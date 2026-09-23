package br.com.seucusto.app
import org.junit.Assert.assertEquals
import org.junit.Test
class CompositionRuleTest{
 private fun status(total:Double)=when{total<99.999->"INCOMPLETE";total<=100.001->"OK";else->"OVER"}
 @Test fun below100IsIncomplete()=assertEquals("INCOMPLETE",status(80.0))
 @Test fun exactly100IsOk()=assertEquals("OK",status(100.0))
 @Test fun above100IsOver()=assertEquals("OVER",status(120.0))
}