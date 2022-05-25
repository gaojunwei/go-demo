--限流脚本,递增计数
local c = redis.call('incr', KEYS[1])
if tonumber(c) == 1
    then redis.call('expire', KEYS[1], ARGV[1])
end
return c;