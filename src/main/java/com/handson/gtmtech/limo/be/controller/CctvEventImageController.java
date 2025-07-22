package com.handson.gtmtech.limo.be.controller;


import org.springframework.beans.factory.annotation.Autowired;
import com.handson.gtmtech.limo.be.entity.CctvEventImage;
import com.handson.gtmtech.limo.be.service.CctvEventImageService;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/cctv-images")
public class CctvEventImageController {
    @Autowired
    private CctvEventImageService cctvEventImageService;

    @GetMapping
    public List<CctvEventImage> getAllCctvEventImages() {
        return cctvEventImageService.getAllCctvEventImages();
    }

    @PostMapping
    public CctvEventImage createCctvEventImage(@RequestBody CctvEventImage cctvEventImage) {
        return cctvEventImageService.saveCctvEventImage(cctvEventImage);
    }

}