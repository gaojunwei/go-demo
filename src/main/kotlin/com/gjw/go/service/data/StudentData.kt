package com.gjw.go.service.data

import com.gjw.go.domain.StudentPo

/**
 * 学生数据
 */
class StudentData {
    companion object {
        private var data = mutableListOf<StudentPo>()

        init {
            for (i in 1..100) {
                var studentPo: StudentPo = StudentPo().apply {
                    name = "小明-$i"
                    age = 18 + i
                    school = "中山大学$i"
                    clzz = "初一三班"
                    teacher = "刘能"
                }
                data.add(studentPo)
            }
        }

        /**
         * 获取集合数据
         */
        fun getAll(): List<StudentPo> {
            return data
        }

        /**
         * 获取指定数据
         */
        fun getById(id: Int): StudentPo? {
            var list = data.filter { studentPo -> studentPo.name == "小明-$id" }
            return list.firstOrNull()
        }
    }
}