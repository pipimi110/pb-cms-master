package com.puboot.module.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.puboot.common.util.CoreConst;
import com.puboot.common.util.DateUtil;
import com.puboot.module.admin.mapper.BizArticleLookMapper;
import com.puboot.module.admin.model.BizArticleLook;
import com.puboot.module.admin.service.BizArticleLookService;
import com.puboot.module.admin.vo.CountVo;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@Service
@AllArgsConstructor
public class BizArticleLookServiceImpl extends ServiceImpl<BizArticleLookMapper, BizArticleLook> implements BizArticleLookService {

    private final BizArticleLookMapper articleLookMapper;

    @Override
    public int checkArticleLook(Integer articleId, String userIp, Date lookTime) {
        return articleLookMapper.checkArticleLook(articleId, userIp, lookTime);
    }

    @Override
    public Map<String, Integer> lookCountByDay(int day) {
        List<CountVo> list = articleLookMapper.lookCountByDay(day);
        Map<String, Integer> lookCountByDayMap = buildRecentDayMap(day + 1);
        if (CollectionUtils.isNotEmpty(list)) {
            lookCountByDayMap.putAll(list.stream().collect(Collectors.toMap(CountVo::getDay, CountVo::getCount)));
        }
        return lookCountByDayMap;
    }

    @Override
    public Map<String, Integer> userCountByDay(int day) {
        List<CountVo> list = articleLookMapper.userCountByDay(day);
        Map<String, Integer> userCountByDayMap = buildRecentDayMap(day + 1);
        if (CollectionUtils.isNotEmpty(list)) {
            userCountByDayMap.putAll(list.stream().collect(Collectors.toMap(CountVo::getDay, CountVo::getCount)));
        }
        return userCountByDayMap;
    }

    private static Map<String, Integer> buildRecentDayMap(int day) {
        Date now = new Date();
        LinkedHashMap<String, Integer> map = Maps.newLinkedHashMap();
        for (int i = day - 1; i >= 0; i--) {
            int count = CoreConst.ENABLE_DEMO_DATA ? RandomUtils.nextInt(20, 100) : 0;
            map.put(DateUtil.format(DateUtil.addDays(now, -i), DateUtil.webFormat), count);
        }
        return map;
    }
}
