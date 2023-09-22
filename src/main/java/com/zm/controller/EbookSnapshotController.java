package com.zm.controller;

import com.zm.resp.CommonResp;
import com.zm.resp.StatisticResp;
import com.zm.service.EbookSnapshotService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/ebook-snapshot")
public class EbookSnapshotController {

    @Resource
    private EbookSnapshotService ebookSnapshotService;

    @GetMapping("/get-statistic")
    public CommonResp getStatistic() {
        List<StatisticResp> statisticResp = ebookSnapshotService.getStatistic();
        //服务器项目初始化时候，如果昨天没有数据返回的时只有今天

        CommonResp<List<StatisticResp>> commonResp = new CommonResp<>();
        commonResp.setContent(statisticResp);
        return commonResp;
    }

//    @GetMapping("/get-30-statistic")
//    public CommonResp get30Statistic() {
//        List<StatisticResp> statisticResp = ebookSnapshotService.get30Statistic();
//        CommonResp<List<StatisticResp>> commonResp = new CommonResp<>();
//        commonResp.setContent(statisticResp);
//        return commonResp;
//    }

}
