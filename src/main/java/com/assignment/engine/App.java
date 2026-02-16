package com.assignment.engine;

import java.util.concurrent.CompletableFuture;

public class App {
    public static void main(String[] args) throws Exception {
        // We use a fixed Workflow ID. In a real app, this would be unique per employee.
        DurableContext context = new DurableContext("onboarding-123");

        System.out.println("--- Starting Employee Onboarding Workflow ---");

        // STEP 1: Create Record (Sequential)
        String employeeId = context.step("create-record", () -> {
            System.out.println("Executing: Creating Employee Record...");
            return "EMP_99";
        });

        // STEPS 2 & 3: Provision Laptop & Access (Parallel)
        // We use CompletableFuture to run these at the same time
        CompletableFuture<String> laptopTask = CompletableFuture.supplyAsync(() -> 
            context.step("provision-laptop", () -> {
                System.out.println("Executing: Provisioning Laptop...");
                return "MacBook Pro Assigned";
            })
        );

        CompletableFuture<String> accessTask = CompletableFuture.supplyAsync(() -> 
            context.step("provision-access", () -> {
                System.out.println("Executing: Granting Server Access...");
                // SIMULATE CRASH: Uncomment the line below to test durability!
                System.exit(0); 
                return "Admin Access Granted";
            })
        );

        // Wait for both parallel steps to finish
        CompletableFuture.allOf(laptopTask, accessTask).join();

        // STEP 4: Send Welcome Email (Sequential)
        context.step("send-email", () -> {
            System.out.println("Executing: Sending Welcome Email to " + employeeId);
            return "Email Sent!";
        });

        System.out.println("--- Workflow Completed Successfully! ---");
    }
}
