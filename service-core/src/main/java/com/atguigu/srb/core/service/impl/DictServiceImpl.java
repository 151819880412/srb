package com.atguigu.srb.core.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.srb.core.listener.ExcelDictDTOListener;
import com.atguigu.srb.core.pojo.dto.ExcelDictDTO;
import com.atguigu.srb.core.pojo.entity.Dict;
import com.atguigu.srb.core.mapper.DictMapper;
import com.atguigu.srb.core.service.DictService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 数据字典 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2021-10-28
 */
@Service
@Slf4j
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    /**
     * @Resource
     * private DictMapper dictMapper;
     * 如果要注入的 Mapper 就是当前 service 下的 Mapper name就可以不用写 @Resource 直接写 baseMapper
     */
    @Resource
    private RedisTemplate redisTemplate;

    @Transactional(rollbackFor = Exception.class)   // 事务
    @Override
    public void importData(InputStream inputStream) {
        EasyExcel.read(inputStream, ExcelDictDTO.class,new ExcelDictDTOListener(baseMapper)).sheet().doRead();
        log.info("Excel 导入成功");
    }

    @Override
    public List<ExcelDictDTO> listDictData() {

        List<Dict> dictList = baseMapper.selectList(null);
        // 创建 ExcelDictDTO 列表，将 Dic 列表转换成 ExcelDictDTO 列表
        ArrayList<ExcelDictDTO> excelDictDTOList = new ArrayList<>(dictList.size());
        dictList.forEach(dict ->{
            ExcelDictDTO excelDictDTO = new ExcelDictDTO();
            BeanUtils.copyProperties(dict,excelDictDTO);
            excelDictDTOList.add(excelDictDTO);
        });
        return excelDictDTOList;
    }


    @Override
    public List<Dict> listByParentId(Long parentId) {

        // 首先查询 redis 中是否存在数据列表
        try {
            log.info("从redis中获取数据");
            List<Dict> distList = (List<Dict>)redisTemplate.opsForValue().get("srv:core:dictList:"+parentId);
            if(distList!=null){
                return distList;
            }
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("redis服务器异常" + ExceptionUtils.getStackTrace(e));
        }
        // 如果存在则从 redis 中直接返回数据列表

        // 如果不存在才查询数据库
        log.info("从数据库中获取数据");
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("parent_id",parentId);
        List<Dict> dictList = baseMapper.selectList(dictQueryWrapper);
        // 填充 hashChildren
        dictList.forEach(dict -> {
            // 判断当前节点是否有子节点,找到当前 dict 下级有没有子节点
            boolean hasChildren = this.hasChildren(dict.getId());
            dict.setHasChildren(hasChildren);
        });

        // 将数据存入 redis
        try {
            log.info("将数据存入redis");
            redisTemplate.opsForValue().set("srb.core:dictListL"+parentId,dictList,5, TimeUnit.MINUTES);
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("redis服务器异常" + ExceptionUtils.getStackTrace(e));
        }

        // 返回数据列表
        return dictList;
    }

    @Override
    public List<Dict> findByDictCode(String dictCode) {

        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("dict_code",dictCode);
        Dict dict = baseMapper.selectOne(dictQueryWrapper);
        return this.listByParentId(dict.getId());
    }

    @Override
    public String getNameByParentDictCodeAndValue(String dictCode, Integer value) {

        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("dict_code",dictCode);
        Dict parentDict = baseMapper.selectOne(dictQueryWrapper);

        if(parentDict == null){
            return "";
        }

        dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper
                .eq("parent_id",parentDict.getId())
                .eq("value",value);
        Dict dict = baseMapper.selectOne(dictQueryWrapper);

        if(dict == null){
            return "";
        }

        return dict.getName();
    }

    /**
     * 判断该节点是否有子节点
     */
    private boolean hasChildren(Long id) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<Dict>().eq("parent_id", id);
        Integer count = baseMapper.selectCount(queryWrapper);
        if(count.intValue() > 0) {
            return true;
        }
        return false;
    }


}