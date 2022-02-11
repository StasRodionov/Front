package com.trade_accounting.components.util;

import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

//
//public  class Report {
//
//
//
//    private static final Logger logger = LoggerFactory.getLogger(Report.class);
//
//
//
//    public void createDocument(OutputStream outStream, String templateName, Map<String, Object> data) {
//        logger.debug("Start creation of document");
//
//        String pathTemplateName = ("E:\\1javae\\1_Prohect\\Project\\2_Prohect_front\\src\\main\\resources\\xls_templates\\purchases_templates\\return\\test\\").concat(templateName).concat(".xls");
//
//        try(
//                InputStream input =  Files.newInputStream(Paths.get(pathTemplateName));
////
//        ) {//1
////                Sheet sheet = workBook.getSheetAt(0);
////                ByteArrayOutputStream bos = new ByteArrayOutputStream();
////                workBook.write(bos);
////                byte[] barray = bos.toByteArray();
////                InputStream is = new ByteArrayInputStream(barray);
//
//            Context context = new Context();
//
//            for (Map.Entry<String, Object> element : data.entrySet()) { // 2
//                context.putVar(element.getKey(), element.getValue());
//            }
//
//            JxlsHelper.getInstance().processTemplate(input, outStream, context); // 3
//
//        } catch (Exception exception) {
//            logger.error("Fail to generate the document", exception);
//        } finally {
//            closeAndFlushOutput(outStream); // 4
//        }
//    }
//
//    private void closeAndFlushOutput(OutputStream outStream) {
//        try {
//            outStream.flush();
//            outStream.close();
//        } catch (IOException exception) {
//            logger.error("Fail to flush and close the output", exception);
//        }
//    }
//}
