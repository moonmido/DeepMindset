package com.DeepMindset.Ai_Service.Models;

public class ModelRequest {

    private String modelId;
    private String inputText;

    // getters and setters
    public String getModelId() {
        return modelId;
    }
    public void setModelId(String modelId) {
        this.modelId = modelId;
    }
    public String getInputText() {
        return inputText;
    }
    public void setInputText(String inputText) {
        this.inputText = inputText;
    }


}
