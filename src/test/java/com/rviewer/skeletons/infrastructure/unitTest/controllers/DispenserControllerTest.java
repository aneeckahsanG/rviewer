package com.rviewer.skeletons.infrastructure.unitTest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rviewer.skeletons.domain.requests.DispenserRequest;
import com.rviewer.skeletons.domain.requests.DispenserSpendingLineRequest;
import com.rviewer.skeletons.domain.responses.DispenserResponse;
import com.rviewer.skeletons.domain.responses.DispenserSpendingLineResponse;
import com.rviewer.skeletons.domain.services.DispenserService;
import net.minidev.json.JSONObject;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DispenserControllerTest {
    @MockBean
    DispenserService dispenserService;
    private static final ObjectMapper om = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    DispenserResponse dispenserResponse;
    DispenserSpendingLineResponse dispenserSpendingLineResponse;
    DispenserSpendingLineResponse.DispenserLineUsage dispenserLineUsage;
    private List<DispenserSpendingLineResponse.DispenserLineUsage> dispenserLineUsages;

    @BeforeEach
    public void init() {
        dispenserResponse = DispenserResponse.builder().id(new String(UUID.randomUUID().toString())).flowVolume(0.64).rate(new BigDecimal(12.25)).build();
        dispenserLineUsage = DispenserSpendingLineResponse.DispenserLineUsage.builder()
                .openedAt(LocalDateTime.parse("2022-01-01T02:40:00"))
                .closedAt(LocalDateTime.parse("2022-01-01T02:45:00"))
                .flowVolume(0.64)
                .totalSpent(new BigDecimal(2352))
                .build();
        dispenserLineUsages = new ArrayList<>(Arrays.asList(dispenserLineUsage));
        dispenserSpendingLineResponse = new DispenserSpendingLineResponse(new BigDecimal(227), dispenserLineUsages);
    }
    @Test
    public void given_dispenser_saveDispenser_returnOk() throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("flow_volume", 0.64);
        when(dispenserService.save(any(DispenserRequest.class))).thenReturn(dispenserResponse);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/dispenser")
                                .content(om.writeValueAsString(obj))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.flow_volume", Matchers.equalTo(0.64)));
    }

    @Test
    public void given_invalidParameter_saveDispenser_return_BadRequest() throws Exception {
        when(dispenserService.save(any(DispenserRequest.class))).thenReturn(dispenserResponse);
        JSONObject obj = new JSONObject();
        obj.put("some_flow_volume", 0.64);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/dispenser")
                                .content(om.writeValueAsString(obj))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(400));
    }
    @Test
    public void given_invalidValue_saveDispenser_return_BadRequest() throws Exception {
        when(dispenserService.save(any(DispenserRequest.class))).thenReturn(dispenserResponse);
        JSONObject obj = new JSONObject();
        obj.put("flow_volume", 0.64);
        obj.put("rate", -12.25);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/dispenser")
                                .content(om.writeValueAsString(obj))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(400));
    }
    @Test
    public void given_dispenserId_getSpending() throws Exception {

        Mockito.when(dispenserService.findAllSpendingLineByDispenserId("38e0e4d4-a480-44da-8910-c43ea560e240")).thenReturn(dispenserSpendingLineResponse);
        mockMvc
                .perform(get("/dispenser/38e0e4d4-a480-44da-8910-c43ea560e240/spending/"))
                .andExpect(status().isOk());
    }
    @Test
    public void given_dispenserLine_saveDispenserLine_return_Ok() throws Exception {
        DispenserSpendingLineResponse.DispenserLineUsage dispenserLineUsage = DispenserSpendingLineResponse.DispenserLineUsage.builder()
                .openedAt(LocalDateTime.parse("2022-01-01T02:40:00"))
                .flowVolume(0.64)
                .totalSpent(new BigDecimal(2352))
                .build();
        JSONObject obj = new JSONObject();
        obj.put("status", "open");
        obj.put("updated_at", "2022-01-01T02:40:00");
        when(dispenserService.saveDispenserSpendingLine(any(String.class), any(DispenserSpendingLineRequest.class))).thenReturn(dispenserLineUsage);
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/dispenser/38e0e4d4-a480-44da-8910-c43ea560e240/status")
                                .content(om.writeValueAsString(obj))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.opened_at", Matchers.equalTo("2022-01-01 02:40:00")));
    }

    @Test
    public void given_invalidParameter_saveDispenserLine_return_BadRequest() throws Exception {
        DispenserSpendingLineResponse.DispenserLineUsage dispenserLineUsage = DispenserSpendingLineResponse.DispenserLineUsage.builder()
                .openedAt(LocalDateTime.parse("2022-01-01T02:40:00"))
                .flowVolume(0.64)
                .totalSpent(new BigDecimal(2352))
                .build();
        JSONObject obj = new JSONObject();
        obj.put("some_status", "open");
        obj.put("updated_at", "2022-01-01T02:40:00");
        when(dispenserService.saveDispenserSpendingLine(any(String.class), any(DispenserSpendingLineRequest.class))).thenReturn(dispenserLineUsage);
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/dispenser/38e0e4d4-a480-44da-8910-c43ea560e240/status")
                                .content(om.writeValueAsString(obj))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(400));
    }
}
