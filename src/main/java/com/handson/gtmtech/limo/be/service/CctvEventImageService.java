package com.handson.gtmtech.limo.be.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.handson.gtmtech.limo.be.controller.DeviceController;
import com.handson.gtmtech.limo.be.entity.CctvEventImage;
import com.handson.gtmtech.limo.be.repository.AzureBlobRepository;
import com.handson.gtmtech.limo.be.repository.CctvEventImageRepository;

import org.slf4j.*;

@Service
public class CctvEventImageService {

    @Autowired
    private CctvEventImageRepository cctvEventImageRepository;

    private final AzureBlobRepository azureBlobRepository;
    
   public CctvEventImageService(AzureBlobRepository azureBlobRepository) {
        this.azureBlobRepository = azureBlobRepository;
    }


    private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);



    public List<CctvEventImage> getAllCctvEventImages() {
        
        return cctvEventImageRepository.findAllByOrderByEventTimestampDesc();
    }




    public List<String> getAllBlobNames() {
        return azureBlobRepository.listBlobs();
    }


    public CctvEventImage saveCctvEventImage(CctvEventImage cctvEventImage) {
        // Blob URL을 생성하여 저장
        // SAS 토큰은 Blob URL에 자동으로 추가되지 않으므로,
        // 필요에 따라 프론트엔드에서 Blob URL에 SAS 토큰을 쿼리 파라미터로 추가해야 합니다.
      //azureBlobRepository.uploadBlob(null, null, 0);
        return cctvEventImageRepository.save(cctvEventImage);
    }




}