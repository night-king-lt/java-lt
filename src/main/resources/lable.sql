insert into label
select rowkey, cf
from (
    select rowkey, Row(userid, `time`, data1, data2) as cf, row_number() over(partition by userid order by `time`) as rn
    from
    (
        select d.userid as rowkey, e.userid as userid, cast(e.`time` as string) as `time`, e.data as data1, d.data as data2
        from
            (
                SELECT userid, `time`, data
                FROM download
            ) d
        left join
            (
                SELECT userid, `time`, data
                FROM exposure
            ) e
        on d.userid = e.userid
            AND d.`time` BETWEEN e.`time` - INTERVAL '30' SECOND AND e.`time` + INTERVAL '30' SECOND
    )
) s
where s.rn = 1