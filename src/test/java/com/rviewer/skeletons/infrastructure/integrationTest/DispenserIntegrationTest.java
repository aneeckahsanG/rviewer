package com.rviewer.skeletons.infrastructure.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.hamcrest.Matchers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:data.sql"})
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DispenserIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    private static final ObjectMapper om = new ObjectMapper();

    @Test
    public void test_Spending_Line_By_valid_Dispenser() throws Exception {
        mockMvc.perform(get("/dispenser/81964bb5-c922-49c9-b912-cbcf454a9e49/spending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount", Matchers.equalTo(210.00)));
    }
    @Test
    public void test_Spending_Line_By_Invalid_Dispenser() throws Exception {
        mockMvc.perform(get("/dispenser/81964bb5-c922-49c9-b912-cbcf454a9e48/spending"))
                .andExpect(status().is(404));
    }
    @Test
    public void test_Create_Dispenser() throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("flow_volume", 0.64);
        mockMvc.perform(post("/dispenser")
                        .content(om.writeValueAsString(obj))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flow_volume", Matchers.equalTo(0.64)));
    }
    @Test
    public void test_Create_Dispenser_With_InvalidParam() throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("some_flow_volume", 0.64);
        mockMvc.perform(post("/dispenser")
                        .content(om.writeValueAsString(obj))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }
    @Test
    public void test_Create_Dispenser_With_InvalidRate() throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("flow_volume", 0.64);
        obj.put("rate", -12.25);
        mockMvc.perform(post("/dispenser")
                        .content(om.writeValueAsString(obj))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }
    @Test
    public void save_Dispenser_Line_Event() throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("status", "open");
        obj.put("updated_at", "2022-01-01T02:40:00");
        mockMvc.perform(put("/dispenser/81964bb5-c922-49c9-b912-cbcf454a9e49/status")
                        .content(om.writeValueAsString(obj))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.flow_volume", Matchers.equalTo(0.5)))
                .andExpect(jsonPath("$.closed_at", Matchers.isEmptyOrNullString()));
    }
    @Test
    public void saveDispenserLine_same_status() throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("status", "close");
        obj.put("updated_at", "2022-01-01T02:40:00");
        mockMvc.perform(put("/dispenser/81964bb5-c922-49c9-b912-cbcf454a9e49/status")
                        .content(om.writeValueAsString(obj))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().is(409));
    }
    @Test
    public void saveDispenserLine_invalid_closingtime() throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("status", "close");
        obj.put("updated_at", "2022-01-01T02:15:00");
        mockMvc.perform(put("/dispenser/81964bb5-c922-49c9-b912-cbcf454a9e99/status")
                        .content(om.writeValueAsString(obj))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }
}
