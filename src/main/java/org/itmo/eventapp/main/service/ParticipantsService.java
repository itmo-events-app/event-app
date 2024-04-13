package org.itmo.eventapp.main.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.itmo.eventapp.main.exceptionhandling.ExceptionConst;
import org.itmo.eventapp.main.minio.MinioService;
import org.itmo.eventapp.main.model.dto.request.ParticipantPresenceRequest;
import org.itmo.eventapp.main.model.entity.Participant;
import org.itmo.eventapp.main.repository.ParticipantsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
import java.util.*;

@RequiredArgsConstructor
@Service
public class ParticipantsService {
    private final ParticipantsRepository participantsRepository;
    private final EventService eventService;
    private final MinioService minioService;
    private static final String BUCKET_NAME = "event-participants";
    private static final String name = "ФИО";
    private static final String email = "Email";
    private static final String phone = "Телефон";

    public List<Participant> getParticipants(Integer id) {
        Optional<List<Participant>> foundParticipants = participantsRepository.findAllByEventId(id);
        List<Participant> participants = foundParticipants.get();
        return participants;
    }

    public Participant changePresence(Integer eventId, ParticipantPresenceRequest participantPresenceRequest){
        Participant participant = participantsRepository.findByIdAndEventId(participantPresenceRequest.participantId(), eventId).get();
        participant.setVisited(participantPresenceRequest.isVisited());
        participantsRepository.save(participant);
        return participant;
    }

    public List<Participant> setParticipants(Integer eventId, MultipartFile participantsListFile) throws IOException {

        savetoMinio(participantsListFile, eventId);
        List<Participant> participants = new ArrayList<>();

        try{
        InputStream list = participantsListFile.getInputStream();
        Workbook workbook = new XSSFWorkbook(list);
        boolean correctColumns = checkColumns(workbook);
        if (!correctColumns) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, ExceptionConst.EXCEL_COLUMNS_ERROR);
        List<Map<String, Object>> data = excelParsing(workbook);

        int counter = 0;
        for(int y = 0; y < data.size(); y++){
            Participant participant = new Participant();
            participant.setName(data.get(counter).get(name).toString());
            participant.setEmail(data.get(counter).get(email).toString());
            participant.setAdditionalInfo(data.get(counter).get(phone).toString());
            participant.setEvent(eventService.findById(eventId));
            participant.setVisited(false);
            participantsRepository.save(participant);
            participants.add(participant);
            counter++;
        }}
        catch(IOException e){
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ExceptionConst.PARTICIPANTS_LIST_PARSING_ERROR);
        }
        return participants;
    }

    private boolean checkColumns(Workbook workbook){
        Sheet excelSheet = workbook.getSheetAt(0);
        Row row = excelSheet.getRow(0);

        int exist = 0;
        for (Cell cell : row) {
            if (cell.getStringCellValue().equals(name) || cell.getStringCellValue().equals(email) || cell.getStringCellValue().equals(phone)) {
                exist++;
            }
        }

        return exist == 3;
    }
    private List<Map<String, Object>> excelParsing(Workbook workbook){

        Sheet excelSheet = workbook.getSheetAt(0);
        List<Map<String, Object>> data = new ArrayList<>();

        for (Row row : excelSheet) {
            outerLoop:
            if(row.getRowNum() != 0) {
                Map<String, Object> participant = new HashMap<>();
                for (Cell cell : row) {
                    String columnName = excelSheet.getRow(0).getCell(cell.getColumnIndex()).getStringCellValue();
                    if (cell.getCellType() == Cell.CELL_TYPE_STRING){
                        participant.put(columnName, cell.getStringCellValue());
                        if(cell.getColumnIndex() == 0 && cell.getStringCellValue().trim().isEmpty()){
                            break outerLoop;
                        }
                    }
                    else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
                        participant.put(columnName, cell.getNumericCellValue());
                    }
                    else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA){
                        participant.put(columnName, cell.getCellFormula());
                    }
                    else if(cell.getCellType() == Cell.CELL_TYPE_BLANK && cell.getColumnIndex() == 0) {
                        break outerLoop;
                    }

                }
                if (participant.isEmpty()) break;
                data.add(participant);
            }
        }

        return data;

    }

    private void savetoMinio(MultipartFile file, Integer id){
        if (!Objects.isNull(file)) {
            String modifiedFileName = id.toString() + "." + FilenameUtils.getExtension(file.getName());
            minioService.uploadWithModifiedFileName(file, BUCKET_NAME, modifiedFileName);
        }
    }

    public String getParticipantsXlsx(Integer eventId) throws IOException {
        List<Participant> participants = getParticipants(eventId);
        Workbook book = new XSSFWorkbook();

        Sheet sheet = book.createSheet("Участники");
        sheet.setColumnWidth(0, 10000);
        sheet.setColumnWidth(1, 10000);
        sheet.setColumnWidth(2, 10000);
        sheet.setColumnWidth(3, 8000);
        sheet.setColumnWidth(4, 6000);

        Row header = sheet.createRow(0);
        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("ФИО");
        headerCell = header.createCell(1);
        headerCell.setCellValue("Email");
        headerCell = header.createCell(2);
        headerCell.setCellValue("Дополнительная информация");
        headerCell = header.createCell(3);
        headerCell.setCellValue("Мероприятие");
        headerCell = header.createCell(4);
        headerCell.setCellValue("Посещение");

        int index = 1;
        for(Participant participant : participants){
            Row row = sheet.createRow(index);
            Cell cell = row.createCell(0);
            cell.setCellValue(participant.getName());
            cell = row.createCell(1);
            cell.setCellValue(participant.getEmail());
            cell = row.createCell(2);
            cell.setCellValue(participant.getAdditionalInfo());
            cell = row.createCell(3);
            cell.setCellValue(participant.getEvent().getTitle());
            cell = row.createCell(4);
            if(participant.isVisited()) {
                cell.setCellValue("Присутствовал(-а)");
            }
            else{
                cell.setCellValue("Не присутствовал(-а)");
            }
            index++;
        }

        File list = new File("/ListOfParticipants.xlsx");

        FileOutputStream outputStream = new FileOutputStream(list);
        book.write(outputStream);
        book.close();

        return list.getAbsolutePath();

    }

}
