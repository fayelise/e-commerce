package com.tp.stock_service.controller;

import com.tp.stock_service.dto.ReserveStockRequest;
import com.tp.stock_service.dto.StockResponse;
import com.tp.stock_service.service.StockService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stock")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping("/reserve")
    public StockResponse reserve(@RequestBody ReserveStockRequest request) {
        return stockService.reserve(request);
    }

    @PostMapping("/{orderId}/release")
    public StockResponse release(@PathVariable String orderId) {
        return stockService.release(orderId);
    }
}
