package com.zm.mapper;

import com.zm.resp.StatisticResp;

import java.util.List;

public interface MyEbookSnapshotMapper {

    void genSnapshot();

    List<StatisticResp> getStatistic();
}
