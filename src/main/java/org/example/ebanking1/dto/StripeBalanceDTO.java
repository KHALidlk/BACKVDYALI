package org.example.ebanking1.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.stripe.model.Balance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A wrapper class for Stripe Balance to avoid serialization issues
 * with Stripe's internal response objects
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StripeBalanceDTO {
    private List<BalanceAmount> available;
    private List<BalanceAmount> pending;
    private String object;
    private Boolean livemode;

    public StripeBalanceDTO() {
    }

    public StripeBalanceDTO(Balance balance) {
        if (balance != null) {
            this.object = balance.getObject();
            this.livemode = balance.getLivemode();
            this.available = new ArrayList<>();
            this.pending = new ArrayList<>();

            try {
                // Log the balance to see its structure
                System.out.println("Balance object: " + balance);

                // Check if available funds exist and process them
                if (balance.getAvailable() != null) {
                    for (Object availableMoney : balance.getAvailable()) {
                        processBalanceAmount(availableMoney, this.available);
                    }
                }

                // Check if pending funds exist and process them
                if (balance.getPending() != null) {
                    for (Object pendingMoney : balance.getPending()) {
                        processBalanceAmount(pendingMoney, this.pending);
                    }
                }
            } catch (Exception e) {
                // Log any errors during processing
                System.err.println("Error processing Stripe balance: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void processBalanceAmount(Object moneyObj, List<BalanceAmount> targetList) {
        try {
            System.out.println("Processing money object: " + moneyObj);
            System.out.println("Money object class: " + (moneyObj != null ? moneyObj.getClass().getName() : "null"));

            if (moneyObj instanceof Map) {
                Map<String, Object> moneyMap = (Map<String, Object>) moneyObj;
                System.out.println("Money map keys: " + moneyMap.keySet());

                // Extract the values with appropriate type handling
                Object amountObj = moneyMap.get("amount");
                Object currencyObj = moneyMap.get("currency");
                Object sourceTypesObj = moneyMap.get("source_types");

                Long amount = null;
                if (amountObj instanceof Number) {
                    amount = ((Number) amountObj).longValue();
                }

                String currency = currencyObj != null ? currencyObj.toString() : null;
                Map<String, Object> sourceTypes = null;

                if (sourceTypesObj instanceof Map) {
                    sourceTypes = (Map<String, Object>) sourceTypesObj;
                }

                System.out.println("Extracted amount: " + amount + ", currency: " + currency);
                targetList.add(new BalanceAmount(amount, currency, sourceTypes));
            } else if (moneyObj != null) {
                // Try to use reflection to access fields if it's not a Map
                try {
                    Class<?> moneyClass = moneyObj.getClass();
                    System.out.println("Money class methods: ");
                    for (java.lang.reflect.Method method : moneyClass.getMethods()) {
                        if (method.getName().startsWith("get") && method.getParameterCount() == 0) {
                            System.out.println(" - " + method.getName());
                        }
                    }

                    // Try common getter methods
                    Long amount = null;
                    String currency = null;
                    Map<String, Object> sourceTypes = null;

                    try {
                        java.lang.reflect.Method getAmount = moneyClass.getMethod("getAmount");
                        Object amountObj = getAmount.invoke(moneyObj);
                        if (amountObj instanceof Number) {
                            amount = ((Number) amountObj).longValue();
                        }
                    } catch (Exception e) {
                        System.err.println("Could not get amount: " + e.getMessage());
                    }

                    try {
                        java.lang.reflect.Method getCurrency = moneyClass.getMethod("getCurrency");
                        Object currencyObj = getCurrency.invoke(moneyObj);
                        currency = currencyObj != null ? currencyObj.toString() : null;
                    } catch (Exception e) {
                        System.err.println("Could not get currency: " + e.getMessage());
                    }

                    try {
                        java.lang.reflect.Method getSourceTypes = moneyClass.getMethod("getSourceTypes");
                        Object sourceTypesObj = getSourceTypes.invoke(moneyObj);
                        if (sourceTypesObj instanceof Map) {
                            sourceTypes = (Map<String, Object>) sourceTypesObj;
                        }
                    } catch (Exception e) {
                        System.err.println("Could not get sourceTypes: " + e.getMessage());
                    }

                    if (amount != null && currency != null) {
                        System.out.println("Created BalanceAmount from reflection: " + amount + " " + currency);
                        targetList.add(new BalanceAmount(amount, currency, sourceTypes));
                    }
                } catch (Exception e) {
                    System.err.println("Error using reflection: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Error in processBalanceAmount: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Nested class to represent balance amounts
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BalanceAmount {
        private String currency;
        private Long amount;
        private Map<String, Object> sourceTypes;

        public BalanceAmount() {
        }

        public BalanceAmount(Long amount, String currency, Map<String, Object> sourceTypes) {
            this.amount = amount;
            this.currency = currency;
            if (sourceTypes != null) {
                this.sourceTypes = new HashMap<>(sourceTypes);
            }
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public Long getAmount() {
            return amount;
        }

        public void setAmount(Long amount) {
            this.amount = amount;
        }

        public Map<String, Object> getSourceTypes() {
            return sourceTypes;
        }

        public void setSourceTypes(Map<String, Object> sourceTypes) {
            this.sourceTypes = sourceTypes;
        }
    }

    public List<BalanceAmount> getAvailable() {
        return available;
    }

    public void setAvailable(List<BalanceAmount> available) {
        this.available = available;
    }

    public List<BalanceAmount> getPending() {
        return pending;
    }

    public void setPending(List<BalanceAmount> pending) {
        this.pending = pending;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public Boolean getLivemode() {
        return livemode;
    }

    public void setLivemode(Boolean livemode) {
        this.livemode = livemode;
    }
}
