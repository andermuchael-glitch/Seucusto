package br.com.seucusto.app.domain
import java.math.BigDecimal
import java.math.RoundingMode
data class CostBreakdown(val material:BigDecimal=BigDecimal.ZERO,val labor:BigDecimal=BigDecimal.ZERO,val fixed:BigDecimal=BigDecimal.ZERO,val variable:BigDecimal=BigDecimal.ZERO,val packaging:BigDecimal=BigDecimal.ZERO,val waste:BigDecimal=BigDecimal.ZERO,val other:BigDecimal=BigDecimal.ZERO){val total get()=material+labor+fixed+variable+packaging+waste+other}
object CostCalculationService{
fun calculateMaterialCost(quantity:BigDecimal,unitCost:BigDecimal)=quantity*unitCost
fun calculateLaborCost(timeHours:BigDecimal,hourlyValue:BigDecimal,workers:BigDecimal)=timeHours*hourlyValue*workers
fun calculateFixedCost(monthly:BigDecimal,estimatedProduction:BigDecimal)=if(estimatedProduction.signum()==0)BigDecimal.ZERO else monthly/estimatedProduction
fun calculateWaste(base:BigDecimal,percent:BigDecimal)=base*percent/BigDecimal(100)
fun calculateTotalCost(b:CostBreakdown)=b.total
fun calculateSellingPrice(cost:BigDecimal,marginPercent:BigDecimal):BigDecimal{require(marginPercent>=BigDecimal.ZERO&&marginPercent<BigDecimal(100));return cost.divide(BigDecimal.ONE-marginPercent/BigDecimal(100),8,RoundingMode.HALF_UP)}
fun calculateMargin(cost:BigDecimal,price:BigDecimal)=if(price.signum()==0)BigDecimal.ZERO else (price-cost)/price*BigDecimal(100)
fun calculateMarkup(cost:BigDecimal,price:BigDecimal)=if(cost.signum()==0)BigDecimal.ZERO else price/cost
}
