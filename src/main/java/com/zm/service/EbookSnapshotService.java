package com.zm.service;

import com.zm.mapper.MyEbookSnapshotMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class EbookSnapshotService {

    @Resource
    private MyEbookSnapshotMapper myEbookSnapshotMapper;

    public void genSnapshot() {
        myEbookSnapshotMapper.genSnapshot();
    }

//    /**
//     * 获取首页数值数据：总阅读数、总点赞数、今日阅读数、今日点赞数、今日预计阅读数、今日预计阅读增长
//     */
//    public List<StatisticResp> getStatistic() {
//        return  myEbookSnapshotMapper.getStatistic();
//    }
//
//    /**
//     * 30天数值统计
//     */
//    public List<StatisticResp> get30Statistic() {
//        return  myEbookSnapshotMapper.get30Statistic();
//    }

}
