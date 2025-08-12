package com.DeepMindset.Ai_Service.Controllers;

import com.DeepMindset.Ai_Service.Services.AiModelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/api/models")
public class AiController {



    private final AiModelService huggingFaceService;

    public AiController( AiModelService huggingFaceService) {
        this.huggingFaceService = huggingFaceService;
    }

    @GetMapping("/search/{prompt}")
    public ResponseEntity<?> search(@PathVariable String prompt) {
        try {
            return ResponseEntity.ok(huggingFaceService.searchModels(prompt));
        }catch (IllegalStateException e){
            return ResponseEntity.badRequest().build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/download")
    public ResponseEntity<String> downloadModel(
            @RequestParam String query,
            @RequestParam String saveDir
    ) {
        try {
           huggingFaceService.downloadModel(query, saveDir);
            return ResponseEntity.ok("Model downloaded successfully to: " + saveDir);
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("Failed to download model: " + e.getMessage());
        }
    }






}
