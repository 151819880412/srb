package com.atguigu.srb.core.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.srb.core.mapper.DictMapper;
import com.atguigu.srb.core.pojo.dto.ExcelDictDTO;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: admin
 * @date: 2021/10/19 15:57
 * @description:
 *
 * 上传 Excel 思路：由于数据库一次最多3000条所以需要创建一个 list 来暂存上传的数据。
 */
@Slf4j
//@AllArgsConstructor //全参
@NoArgsConstructor //无参
public class ExcelDictDTOListener extends AnalysisEventListener<ExcelDictDTO> {

    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5;
    List<ExcelDictDTO> list = new ArrayList();

    private DictMapper dictMapper;

    //传入mapper对象
    public ExcelDictDTOListener(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

    /**
     *遍历每一行的记录
     * @param data
     * @param context
     */
    @Override
    public void invoke(ExcelDictDTO data, AnalysisContext context) {
        log.info("解析到一条记录: {}", data);
        list.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            list.clear();
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库。用来存不足以5条的数据
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", list.size());
        // 调用 Mapper 层的批量保存方法。保存 list 对象
        dictMapper.insertBatch(list);  //批量插入
        log.info("存储数据库成功！");
    }
}