--点赞操作，写入并自增，如果写入失败则不自增，【原子性、幂等性】
--if redis.call('SETNX',KEYS[1],1) == 1
--then
--    redis.call('EXPIRE',KEYS[1],864000)
--    redis.call('INCR',KEYS[2])
--end
--return redis.call('GET',KEYS[2])
array = {}
local score = redis.call('ZSCORE', KEYS[2], ARGV[1])

if score
then
    --score 非空则取消点赞
    redis.call('ZREM', KEYS[2], ARGV[1])
    array[0] = 0
else
    -- 点赞操作
    redis.call('ZADD', KEYS[2], ARGV[1],ARGV[2])
    array[0] = 1
end
    --点赞数量
    array[1] = redis.call('ZLEXCOUNT',KEYS[2],'-','+')
    return array

