package dev.bandurski.dao;

import dev.bandurski.model.IncomeSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JdbcIncomeSourceDaoTests extends BaseDaoTests {

    private static final IncomeSource SOURCE_1;
    private static final IncomeSource SOURCE_2;

    private static final List<IncomeSource> ALL_SOURCES = new ArrayList<>();

    static {
        SOURCE_1 = mapValuesToIncomeSource(1, "Other", new BigDecimal("1000.45"));
        SOURCE_2 = mapValuesToIncomeSource(2, "Salary", new BigDecimal("5000.21"));

        ALL_SOURCES.add(SOURCE_2);
        ALL_SOURCES.add(SOURCE_1);
    }

    private JdbcIncomeSourceDao dao;

    @Before
    public void setup() {
        dao = new JdbcIncomeSourceDao(dataSource);
    }

    @Test
    public void getIncomeSources_returns_sources_in_correct_order() {
        List<IncomeSource> sources = dao.getIncomeSources();
        Assert.assertEquals("getIncomeSources() returned incorrect number of sources",
                ALL_SOURCES.size(), sources.size());

        for (int i = 0; i < ALL_SOURCES.size(); i++) {
            assertIncomeSourcesMatch("source at position " + i + " (zero indexed) " +
                    "did not match on column: ", ALL_SOURCES.get(i), sources.get(i));
        }
    }

    @Test
    public void getIncomeSourceById_returns_correct_source() {
        IncomeSource source = dao.getIncomeSourceById(SOURCE_2.getIncomeSourceId());

        assertIncomeSourcesMatch(
            "getIncomeSourceById("+SOURCE_2.getIncomeSourceId()+") did not match on column: ",
            SOURCE_2,
            source
        );
    }

    @Test
    public void createIncomeSource_inserts_source_with_expected_values() {
        IncomeSource newIncomeSource = mapValuesToIncomeSource(999,
                "Mobile Pharmaceuticals", new BigDecimal("999.99"));

        IncomeSource createdIncomeSource = dao.createIncomeSource(newIncomeSource);
        int newId = createdIncomeSource.getIncomeSourceId();
        Assert.assertNotEquals("incomeSourceId not set when created, remained 0",
                0, newId);

        newIncomeSource.setIncomeSourceId(newId);
        IncomeSource retrievedIncomeSource = dao.getIncomeSourceById(newId);
        assertIncomeSourcesMatch("created incomeSource did not match on column: ",
                newIncomeSource, retrievedIncomeSource);
    }

    @Test
    public void updateIncomeSource_updates_source_with_expected_values() {
        IncomeSource incomeSourceToUpdate = mapValuesToIncomeSource(SOURCE_1.getIncomeSourceId(),
                "Some Source", new BigDecimal("9999.99"));

        dao.updateIncomeSource(incomeSourceToUpdate);
        IncomeSource retrievedIncomeSource = dao.getIncomeSourceById(SOURCE_1.getIncomeSourceId());

        assertIncomeSourcesMatch("updated incomeSource did not match on column: ",
                incomeSourceToUpdate, retrievedIncomeSource);
    }

    @Test
    public void deleteIncomeSourceById_source_can_no_longer_be_retrieved() {
        dao.deleteIncomeSourceById(SOURCE_2.getIncomeSourceId());

        IncomeSource retrievedSource = dao.getIncomeSourceById(SOURCE_2.getIncomeSourceId());
        Assert.assertNull("deleted incomeSource can still be retrieved", retrievedSource);
    }

    private static IncomeSource mapValuesToIncomeSource(int incomeSourceId, String name,
                                                        BigDecimal amount) {
        IncomeSource incomeSource = new IncomeSource();
        incomeSource.setIncomeSourceId(incomeSourceId);
        incomeSource.setName(name);
        incomeSource.setAmount(amount);

        return incomeSource;
    }

    private void assertIncomeSourcesMatch(String message, IncomeSource expected, IncomeSource actual) {
        Assert.assertEquals(message + "income_source_id", expected.getIncomeSourceId(),
                actual.getIncomeSourceId());
        Assert.assertEquals(message + "name", expected.getName(),
                actual.getName());
        Assert.assertEquals(message + "amount", expected.getAmount(),
                actual.getAmount());
    }
}
