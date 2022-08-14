package recon.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import recon.matching.ComparisonResult;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ComparisonControllerTest {

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    void handleComparison() throws Exception {

        byte[] firstFileBytes = Files.readAllBytes(Paths.get(this.getClass().getClassLoader().getResource("c1.csv").toURI()));;
        byte[] secondFileBytes = Files.readAllBytes(Paths.get(this.getClass().getClassLoader().getResource("p1.csv").toURI()));;
        MockMultipartFile firstFile = new MockMultipartFile("firstFile", "c1.txt", "text/plain", firstFileBytes);
        MockMultipartFile secondFile = new MockMultipartFile("secondFile", "cp1.txt", "text/plain", secondFileBytes);

        final MvcResult mvcResult = mockMvc.perform(multipart("/compare")
                        .file(firstFile)
                        .file(secondFile)
                .param("similarity-fields", "narrative, amount")
                )

                .andExpect(status().is(200)).andReturn();
        final String contentAsString = mvcResult.getResponse().getContentAsString();
        final ComparisonResult comparisonResult = mapper.readValue(contentAsString, ComparisonResult.class);
        assertEquals(306, comparisonResult.getFirstSummary().getTotal());
        assertEquals(306, comparisonResult.getSecondSummary().getTotal());
        assertEquals(11, comparisonResult.getUnmatchedRecords().size());
        assertEquals(4, comparisonResult.getSecondSummary().getSimilar());
    }
}