/**
 * @Author - Richard Renaud
 * Integration test for ubiCov/Improbable web application.
 * This will give us a full spring Boot instance which will run on a random port.
 */
package com.ubicov.app;

import com.ubicov.app.domain.Furlough;
import com.ubicov.app.domain.GeoLocation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private List geoLocationsList = new ArrayList<GeoLocation>();

    private List furloughList = new ArrayList<Furlough>();

    public void setUp() {
        // Initialise Gelocation
        geoLocationsList.add(new GeoLocation("E09000001", "City of London", "-0.07969517196215747", "51.5196779388711"));
        geoLocationsList.add(new GeoLocation("E09000002", "Barking and Dagenham", "0.08406889679520191", "51.53197153659997"));
        geoLocationsList.add(new GeoLocation("E09000003", "Barnet", "-0.19885620792410919", "51.64894479224796"));

        //Initialise furlough data for December 2020
        furloughList.add(new Furlough("E09000001", "City of London", LocalDate.parse("2020-12-31"), 7100, 8100, 15200));
        furloughList.add(new Furlough("E09000002", "Barking and Dagenham", LocalDate.parse("2020-12-31"), 7600, 7900, 15500));
        furloughList.add(new Furlough("E09000003", "Barnet", LocalDate.parse("2020-01-31"), 15300, 15000, 30300));

        //Initialise furlough data for January 2021
        furloughList.add(new Furlough("E09000001", "City of London", LocalDate.parse("2021-01-31"), 7600, 8500, 16100));
        furloughList.add(new Furlough("E09000002", "Barking and Dagenham", LocalDate.parse("2021-01-31"), 8400, 8400, 16800));
        furloughList.add(new Furlough("E09000003", "Barnet", LocalDate.parse("2021-01-31"), 17000, 16000, 33000));
    }

    @Test
    public void test_geolocation_integration() throws Exception {
        //arrange

        //act
        ResponseEntity<GeoLocation> response = restTemplate.getForEntity("/loc/City of London", GeoLocation.class);

        //assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getDistrict()).isEqualTo("City of London");
        assertThat(response.getBody().getLadCode()).isEqualTo("E09000001");
        assertThat(response.getBody().getLongitude()).isEqualTo("-0.07969517196215747");
        assertThat(response.getBody().getLatitude()).isEqualTo("51.5196779388711");
    }

    // TODO - add date search criteria to this test case
    @Test
    public void test_furlough_integration() throws Exception {
        // arrange

        //act
        ResponseEntity<Furlough> response = restTemplate.getForEntity("/furlough/City of London", Furlough.class);
        //assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getDistrict()).isEqualTo("City of London");
        assertThat(response.getBody().getLadCode()).isEqualTo("E09000001");
        assertThat(response.getBody().getDate()).isEqualTo(LocalDate.parse("2020-12-31"));
        assertThat(response.getBody().getFemale_furloughed()).isEqualTo(7100);
        assertThat(response.getBody().getMale_furloughed()).isEqualTo(8100);
        assertThat(response.getBody().getTotal_furloughed()).isEqualTo(15200);
    }

//    @Test
//    public void getCovidData_returnsCovidData() {
//        //arrange
//
//        //act
//        ResponseEntity<CovidData> response = restTemplate.getForEntity("/covid/Barking and Dagenham", CovidData.class);
//
//        // assert
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(response.getBody().getLtla_name()).isEqualTo("Barking and Dagenham");
//        assertThat(response.getBody().getLtla_code()).isEqualTo("E09000002");
//        assertThat(response.getBody().getStart_date()).isEqualTo(LocalDate.parse("08/12/2020"));
//        assertThat(response.getBody().getEnd_date()).isEqualTo(LocalDate.parse("21/02/2021"));
//        assertThat(response.getBody().getDose()).isEqualTo(LocalDate.parse("1st dose"));
//        assertThat(response.getBody().getAge()).isEqualTo(LocalDate.parse("71-74"));
//        assertThat(response.getBody().getVaccines()).isEqualTo(4059);
//        assertThat(response.getBody().getPopulation()).isEqualTo(4839);
//        assertThat(response.getBody().getPercentage_vaccine()).isEqualTo(0.839330025);
//    }

}
