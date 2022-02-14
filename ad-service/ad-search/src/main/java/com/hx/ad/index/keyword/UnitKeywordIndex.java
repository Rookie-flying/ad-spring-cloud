package com.hx.ad.index.keyword;

import com.hx.ad.index.IndexAware;
import com.hx.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Slf4j
@Component
public class UnitKeywordIndex implements IndexAware<String, Set<Long>> {

    private static Map<String ,Set<Long>> keywordUnitMap; //倒排索引 一个关键词限制对应多个推广单元
    private static Map<Long, Set<String>> unitKeywordMap; //正向索引 一个推广单元对应多个关键词限制

    static {
        keywordUnitMap = new ConcurrentHashMap<>();
        unitKeywordMap = new ConcurrentHashMap<>();
    }


    @Override
    public Set<Long> get(String key) {

        if(StringUtils.isEmpty(key)) {
            return Collections.emptySet();
        }
        Set<Long> result = keywordUnitMap.get(key);
        if ( null == result) {
            return Collections.emptySet();
        }

        return result;
    }

    @Override
    public void add(String key, Set<Long> value) {

        log.info("unitKeywordIndex, before add: {}",
                unitKeywordMap);
        // 根据key,去keywordUnitMap集合中查找，如果找不到，则创建一个新的value，然后存到Map集合中
        Set<Long> unitIdSet = CommonUtils.getCreate(
                key, keywordUnitMap,
                ConcurrentSkipListSet::new
        );
        unitIdSet.addAll(value);

        for(Long unitId : value) {
            Set<String> keywordSet = CommonUtils.getCreate(
                    unitId, unitKeywordMap,
                    ConcurrentSkipListSet::new
            );
            keywordSet.add(key);
        }

        log.info("UnitKeywordIndex, after add: {}", unitKeywordMap);
    }

    @Override
    public void update(String key, Set<Long> value) {
        // 更新成本大，建议先删除再重新建立
        log.error("keyword index can not not support update");
    }

    @Override
    public void delete(String key, Set<Long> value) {

        log.info("unitKeywordIndex, before delete: {}", unitKeywordMap);

        Set<Long> unitIds = CommonUtils.getCreate(
                key, keywordUnitMap,
                ConcurrentSkipListSet::new
        );
        unitIds.removeAll(value);

        for (Long unitId : value) {

            Set<String> keywordSet = CommonUtils.getCreate(
                    unitId, unitKeywordMap,
                    ConcurrentSkipListSet::new
            );
            keywordSet.remove(key);
        }

        log.info("unitKeywordIndex, after delete: {}", unitKeywordMap);
    }

    public boolean match(Long unitId, List<String> keywords) {

        if (unitKeywordMap.containsKey(unitId)
                && CollectionUtils.isNotEmpty(unitKeywordMap.get(unitId))) {

            Set<String> unitKeywords = unitKeywordMap.get(unitId);
            // 如果keywords是unitKeywords的子集
            return CollectionUtils.isSubCollection(keywords, unitKeywords);
        }
        return false;
    }
    //倒排索引
}
