package com.gjw.common.utils.filedownload;

import com.alibaba.fastjson.JSON;
import com.gjw.common.utils.UuidUtils;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
public class HttpFileDownloadUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpFileDownloadUtil.class);

    private static Long splitSize = 1024 * 1024 * 10l;//分割单位，单位：byte
    private static boolean DEBUG = Boolean.TRUE;
    private static short maxRetryTimes = 3;//下载失败最大重试次数
    private static Map<String, String> suffixMap = Maps.newHashMap();
    //下载文件的后缀关系
    static {
        suffixMap.put("video/mp4", ".mp4");
        suffixMap.put("application/zip", ".zip");
        suffixMap.put("image/jpeg", ".jpg");
        suffixMap.put("image/png", ".png");
        suffixMap.put("image/gif", ".gif");
        suffixMap.put("application/msword", ".doc");
        suffixMap.put("application", ".doc");
        suffixMap.put("application/vnd.ms-excel", ".xls");
        suffixMap.put("application/vnd.ms-powerpoint", ".ppt");
        suffixMap.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document", ".docx");
        suffixMap.put("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", ".xlsx");
        suffixMap.put("application/vnd.openxmlformats-officedocument.presentationml.presentation", ".pptx");
        suffixMap.put("application/pdf", ".pdf");
        suffixMap.put("application/xml", ".xml");
        suffixMap.put("application/json", ".json");
        suffixMap.put("text/html", ".html");
    }

    private static ExecutorService threadPool = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 10L,
            TimeUnit.SECONDS, new SynchronousQueue<Runnable>(true));

    public static void main(String[] args) {
        try {
            String url = "http://jscss.winshangdata.com/seajs/util/citytab/area.data.js?v=20181225v1";
            String basePath = "D:\\www\\";//文件保存的目录
            String fileName = "json数据";//文件名称
            FileDownLoadResult result = downLoadFile(url, basePath, fileName);
            logger.info("下载结果：{}", JSON.toJSONString(result));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        } catch (ExecutionException e) {
            logger.error(e.getMessage(), e);
        } catch (TimeoutException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 文件下载
     *
     * @param url
     * @param saveFilePath
     * @param fileName
     * @return
     * @throws IOException
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    public static FileDownLoadResult downLoadFile(String url, String saveFilePath, String fileName) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        FileDownLoadResult result = new FileDownLoadResult();

        Date startDownLoad = new Date();
        String mainFileName = StringUtils.isEmpty(fileName) ? UuidUtils.getUUID() : fileName;

        /** 获取资源信息，并切片 */
        VideoInfo videoInfo = getLength(url);
        // 文件后缀
        String suffix = judgeSuffix(videoInfo.getContentType());
        // 文件切片
        long contentLength = videoInfo.getContentLength();
        List<DataInfo> dataInfos = splitLength(contentLength);

        dataInfos.stream().forEach(dataInfo -> {
            dataInfo.setUrl(url);
            dataInfo.setContentLength(videoInfo.getContentLength());
            dataInfo.setContentType(videoInfo.getContentType());
            dataInfo.setMainFileName(mainFileName);
            dataInfo.setSuffix(suffix);
            if (dataInfos.size() == 1)
                dataInfo.setFilePath(String.format("%s%s%s", saveFilePath, mainFileName, dataInfo.getSuffix()));
            else
                dataInfo.setFilePath(String.format("%s%s_%s_%s_%s%s", saveFilePath, mainFileName, dataInfo.getStartIndex(), dataInfo.getEndIndex(), dataInfo.getIndex(), dataInfo.getSuffix()));
        });

        /** 下载文件 */
        List<Future<DataInfo>> futureList = new ArrayList<>();
        for (DataInfo dataInfo : dataInfos) {
            Map<String, String> headers = Maps.newHashMap();
            headers.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 10_3 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) CriOS/56.0.2924.75 Mobile/14E5239e Safari/602.1");
            headers.put("Range", String.format("bytes=%s-%s", dataInfo.getStartIndex(), dataInfo.getEndIndex()));
            Future<DataInfo> future = threadPool.submit(() -> doWork(headers, dataInfo));
            futureList.add(future);
        }

        boolean flag = true;
        List<DataInfo> files = new ArrayList<>();
        for (Future<DataInfo> future : futureList) {
            DataInfo dataInfo = future.get(200, TimeUnit.SECONDS);
            if (!dataInfo.getStatus()) {
                flag = false;
            }
            files.add(dataInfo);
        }
        Date endDownLoad = new Date();
        if (DEBUG) {
            long second = (endDownLoad.getTime() - startDownLoad.getTime()) / 1000;
            if(second != 0){
                long rate = contentLength / (1024 * second);
                logger.info("文件下载-耗时：{}秒 下载速率：{}kb/s 共计：{}kb", (endDownLoad.getTime() - startDownLoad.getTime()) / 1000, rate, contentLength / 1024);
            }
        }
        result.setDataInfos(files);
        /** 下载失败 */
        if (!flag)
            return result;

        /** 文件合并 */
        String outFilePath;
        if (files.size() == 1) {
            outFilePath = files.get(0).getFilePath();
        } else {
            List<String> filePaths = files.stream().sorted(Comparator.comparing(DataInfo::getIndex)).collect(Collectors.toList()).stream().map(rangeInfo -> rangeInfo.getFilePath()).collect(Collectors.toList());
            logger.info("合并的文件 ", JSON.toJSONString(filePaths));
            Date startMerge = new Date();
            outFilePath = String.format("%s%s%s", saveFilePath, mainFileName, suffix);
            videoUnionNio(filePaths, outFilePath);
            if (DEBUG) {
                Date endMerge = new Date();
                logger.info("文件合并-耗时：" + (endMerge.getTime() - startMerge.getTime()) + "毫秒");
            }
        }
        result.setDataInfos(files);
        result.setFilePath(outFilePath);
        return result;
    }

    /**
     * 鉴别资源文件的类型
     *
     * @return
     */
    public static String judgeSuffix(String contentType) {
        String suffix = suffixMap.get(contentType.split(";")[0].trim().toLowerCase());
        if (StringUtils.isEmpty(suffix))
            suffix = "";
        return suffix;
    }

    /**
     * 计算Range信息
     *
     * @param contentLength
     * @return
     */
    public static List<DataInfo> splitLength(Long contentLength) {
        List<DataInfo> list = new ArrayList<>();
        Long startIndex = 0l;
        int index = 0;
        while (contentLength > splitSize) {
            DataInfo dataInfo = new DataInfo();
            dataInfo.setIndex(index);
            dataInfo.setStartIndex(startIndex);
            long endIndex = startIndex + splitSize;
            if (endIndex >= contentLength) {
                endIndex = contentLength;
                dataInfo.setEndIndex(endIndex);
                list.add(dataInfo);
                break;
            }
            dataInfo.setEndIndex(endIndex);
            list.add(dataInfo);
            startIndex = endIndex + 1;
            index++;
        }
        if (list.isEmpty()) {
            DataInfo dataInfo = new DataInfo();
            dataInfo.setStartIndex(0l);
            dataInfo.setEndIndex(contentLength);
            dataInfo.setIndex(0);
            list.add(dataInfo);
        }
        return list;
    }

    /**
     * 资源信息
     */
    @Data
    public static class VideoInfo {
        private Long contentLength;
        private String contentType;
    }

    /**
     * 获取文件长度
     *
     * @return
     * @throws IOException
     */
    public static VideoInfo getLength(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Range", "bytes=0-");
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 10_3 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) CriOS/56.0.2924.75 Mobile/14E5239e Safari/602.1");

        CloseableHttpResponse response = null;
        try {
            response = HttpClientTool.getHttpClient().execute(httpGet);
            if (DEBUG)
                logger.info("getContentLength : {} ContentType : {}", response.getEntity().getContentLength(), response.getEntity().getContentType().toString());
            VideoInfo videoInfo = new VideoInfo();
            videoInfo.setContentLength(response.getEntity().getContentLength());
            videoInfo.setContentType(response.getEntity().getContentType().getValue());
            return videoInfo;
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    /**
     * 分片下载文件
     */
    public static DataInfo doWork(Map<String, String> headers, DataInfo dataInfo) {
        HttpGet httpGet = new HttpGet(dataInfo.getUrl());
        //设置请求头
        if (headers != null && headers.size() > 0) {
            for (String key : headers.keySet()) {
                httpGet.setHeader(key, headers.get(key));
            }
        }
        short reTryTimes = 0;
        while (reTryTimes <= maxRetryTimes) {
            CloseableHttpResponse response = null;
            try {
                response = HttpClientTool.getHttpClient().execute(httpGet);
                writeToLocal(dataInfo.getFilePath(), response.getEntity().getContent());
                dataInfo.setStatus(true);
                dataInfo.setRemark("success");
                break;
            } catch (Exception e) {
                logger.error("文件下载异常失败 saveFilePath：{} {}", dataInfo.getFilePath(), e.getMessage(), e);
                reTryTimes++;
                dataInfo.setStatus(false);
                dataInfo.setRemark("fail");
            } finally {
                if (response != null) {
                    try {
                        response.close();
                    } catch (IOException e) {
                        logger.error("文件下载异常失败-流关闭异常失败 saveFilePath：{} {}", dataInfo.getFilePath(), e.getMessage(), e);
                    }
                }
            }
        }
        dataInfo.setRetryTimes(reTryTimes);
        return dataInfo;
    }

    /**
     * 保存文件
     *
     * @param destination
     * @param input
     * @throws IOException
     */
    public static void writeToLocal(String destination, InputStream input) throws IOException {
        int index;
        byte[] bytes = new byte[1024 * 50];
        FileOutputStream downloadFile = new FileOutputStream(destination);
        while ((index = input.read(bytes)) != -1) {
            downloadFile.write(bytes, 0, index);
            downloadFile.flush();
            logger.info("下载中 {}字节", index);
        }
        input.close();
        downloadFile.close();
    }

    /**
     * 合并文件
     *
     * @throws Exception
     */
    public static void videoUnionNio(List<String> files, String outFilePath) throws IOException {
        FileOutputStream writer = null;
        FileInputStream reader = null;
        try {
            File combineFile = new File(outFilePath);
            if (combineFile.exists() && combineFile.isFile()) {
                combineFile.delete();
            }
            writer = new FileOutputStream(combineFile, true);
            for (String filePath : files) {
                File part = new File(filePath);
                if (!part.exists())
                    throw new IOException(String.format("%s file not exist", filePath));
                reader = new FileInputStream(part);
                reader.getChannel().transferTo(0, part.length(), writer.getChannel());
                reader.close();
            }
        } finally {
            if (writer != null)
                writer.close();
            if (reader != null)
                reader.close();
        }
    }
}
