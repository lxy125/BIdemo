package com.yupi.springbootinit.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Slf4j
public class ExcelUtils {
    public static String excelToCsv(MultipartFile multipartFile)   {
//        File file= null;
//        try {
//            file = ResourceUtils.getFile("classpath:test.xlsx");
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
        List<Map<Integer, String>> list = null;
        try {
            list = EasyExcel.read(multipartFile.getInputStream())
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet()
                    .headRowNumber(0)
                    .doReadSync();
        } catch (IOException e) {
            log.error("表格处理错误",e);
            throw new RuntimeException(e);
        }


        StringBuilder stringBuilder=new StringBuilder();

        //转化为csv
        LinkedHashMap<Integer,String> headermap=(LinkedHashMap)list.get(0);
        List<String> headerlist=headermap.values().stream().filter(ObjectUtils::isNotEmpty).collect(Collectors.toList());
        stringBuilder.append(StringUtils.join(headerlist,",")).append("\n");


        for (int i = 1; i < list.size(); i++) {
            LinkedHashMap<Integer,String> map=(LinkedHashMap)list.get(i);
            List<String> dataList=map.values().stream().filter(ObjectUtils::isNotEmpty).collect(Collectors.toList());

            stringBuilder.append(StringUtils.join(dataList,",")).append("\n");


        }


        return stringBuilder.toString();
    }


    public static void main(String[] args) {
        ExcelUtils excelUtils = new ExcelUtils();
        excelUtils.excelToCsv(null);
    }
}
