package com.trade_accounting.components.apps.impl.warehouse;

import com.trade_accounting.components.apps.impl.CallExecuteService;
import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.warehouse.MovementDto;
import com.trade_accounting.models.dto.warehouse.WarehouseDto;
import com.trade_accounting.services.interfaces.warehouse.MovementService;
import com.trade_accounting.services.api.warehouse.MovementApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.SystemProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MovementServiceImpl implements MovementService {

    private final MovementApi movementApi;
    private final String movementUrl;
    private final CallExecuteService<MovementDto> callExecuteService;
    private final String torg13Path = SystemProperties.getProperty("user.dir") + "\\src\\main\\resources\\files\\torg13.xls";
    private final String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

    public MovementServiceImpl(Retrofit retrofit, @Value("${movement_url}") String movementUrl, CallExecuteService<MovementDto> callExecuteService) {
        movementApi = retrofit.create(MovementApi.class);
        this.movementUrl = movementUrl;
        this.callExecuteService = callExecuteService;
    }


    @Override
    public List<MovementDto> getAll() {
        Call<List<MovementDto>> movementDtoListCall = movementApi.getAll(movementUrl);
        return callExecuteService.callExecuteBodyList(movementDtoListCall, MovementDto.class);
    }

    @Override
    public MovementDto getById(Long id) {
        Call<MovementDto> movementDtoCall = movementApi.getById(movementUrl, id);
        return callExecuteService.callExecuteBodyById(movementDtoCall, MovementDto.class, id);
    }

    @Override
    public MovementDto create(MovementDto movementDto) {
        Call<MovementDto> movementDtoCall = movementApi.create(movementUrl, movementDto);

        try {
            movementDto = movementDtoCall.execute().body();
            log.info("Успешно выполнен запрос на создание Movement");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение MovementDto - {}", e);
        }

        return movementDto;
    }

    @Override
    public void update(MovementDto movementDto) {
        Call<Void> movementDtoCall = movementApi.update(movementUrl, movementDto);
        try {
            movementDtoCall.execute();
            log.info("Успешно выполнен запрос на обновление экземпляра Movement");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на обновление экземпляра MovementDto - {}", e);
        }
    }

    @Override
    public List<MovementDto> searchByFilter(Map<String, String> queryMovement) {
        List<MovementDto> movementDtoList = new ArrayList<>();
        Call<List<MovementDto>> callSupplier = movementApi.searchByFilter(movementUrl, queryMovement);
        try {
            movementDtoList = callSupplier.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение перемещений по фильтру {}", movementDtoList);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение перемещений {IOException}", e);
        }
        return movementDtoList;
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> movementDtoCall = movementApi.deleteById(movementUrl, id);
        callExecuteService.callExecuteBodyDelete(movementDtoCall, MovementDto.class, id);
    }

    @Override
    public void moveToIsRecyclebin(Long id) {
        Call<Void> movementDtoCall = movementApi.moveToIsRecyclebin(movementUrl, id);
        callExecuteService.callExecuteBodyMoveToIsRecyclebin(movementDtoCall, MovementDto.class, id);
    }

    @Override
    public void restoreFromIsRecyclebin(Long id) {
        Call<Void> movementDtoCall = movementApi.restoreFromIsRecyclebin(movementUrl, id);
        callExecuteService.callExecuteBodyRestoreFromIsRecyclebin(movementDtoCall, MovementDto.class, id);

    }

    @Override
    public void updateTorg13(MovementDto movementDto, CompanyDto companyDto, WarehouseDto warehouseDto) {
        try (FileInputStream fileInputStream = new FileInputStream(torg13Path)) {
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);

            sheet.getRow(15).getCell(0).setCellValue(companyDto.getName());
            sheet.getRow(15).getCell(3).setCellValue(warehouseDto.getName());
            sheet.getRow(10).getCell(6).setCellValue(movementDto.getId());
            sheet.getRow(10).getCell(7).setCellValue(date);
            fileInputStream.close();

            FileOutputStream outputStream = new FileOutputStream(torg13Path);
            workbook.write(outputStream);
            outputStream.close();

        } catch (IOException ex) {
            log.error("Произошла ошибка при обновлении ТОРГ-13");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
