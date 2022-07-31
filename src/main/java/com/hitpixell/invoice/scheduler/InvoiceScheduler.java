package com.hitpixell.invoice.scheduler;

import com.hitpixell.invoice.service.InvoiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Scheduler for Minute/Daily/Monthly generation of billings
 */
@Service
public class InvoiceScheduler {

    @Autowired
    InvoiceService invoiceService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Scheduled(cron = "${invoice.minutes.cron}", zone = "GMT+4")
    public void generateInvoiceAtEveryMinutes() throws Exception {
        logger.info("Minutes Scheduler run at {}" , LocalDateTime.now());
        invoiceService.generateInvoice("Pizza House 1");
    }

    @Scheduled(cron = "${invoice.daily.cron}", zone = "GMT+4")
    public void generateInvoiceAtEveryDay() throws Exception {
        logger.info("daily Scheduler run at {}" , LocalDateTime.now());
        invoiceService.generateInvoice("Pizza House 1");
    }

    @Scheduled(cron = "${invoice.monthly.cron}", zone = "GMT+4")
    public void generateInvoiceAtEveryMonth() throws Exception {
        logger.info("monthly Scheduler run at {}" , LocalDateTime.now());
        invoiceService.generateInvoice("Pizza House 3");
    }
}
