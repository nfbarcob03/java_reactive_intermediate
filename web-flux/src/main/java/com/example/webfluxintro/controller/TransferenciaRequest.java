package com.example.webfluxintro.controller;

import com.example.webfluxintro.model.TransferenciaDTO;

import java.util.List;

public class TransferenciaRequest {
    private List<TransferenciaDTO> transferencias;

    public List<TransferenciaDTO> getTransferencias() {
        return transferencias;
    }

    public void setTransferencias(List<TransferenciaDTO> transferencias) {
        this.transferencias = transferencias;
    }
}