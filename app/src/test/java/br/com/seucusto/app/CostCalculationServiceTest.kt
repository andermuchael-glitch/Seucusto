package br.com.seucusto.app
import br.com.seucusto.app.domain.CostCalculationService
import java.math.BigDecimal
import org.junit.Assert.assertEquals
import org.junit.Test
class CostCalculationServiceTest{
@Test fun materialCost()=assertEquals(BigDecimal("16.00"),CostCalculationService.calculateMaterialCost(BigDecimal("0.8"),BigDecimal("20.00")))
@Test fun laborCost()=assertEquals(BigDecimal("30.00"),CostCalculationService.calculateLaborCost(BigDecimal("1.5"),BigDecimal("10.00"),BigDecimal("2")))
@Test fun waste()=assertEquals(BigDecimal("5.00"),CostCalculationService.calculateWaste(BigDecimal("100"),BigDecimal("5")))
@Test fun marginPrice()=assertEquals(BigDecimal("142.85714286"),CostCalculationService.calculateSellingPrice(BigDecimal("100"),BigDecimal("30")).setScale(8))
@Test fun markup()=assertEquals(BigDecimal("1.5"),CostCalculationService.calculateMarkup(BigDecimal("100"),BigDecimal("150")))
}
