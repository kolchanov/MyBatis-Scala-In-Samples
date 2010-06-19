package org.mybatis_scala.samples

import org.junit._
import Assert._

@Test
class AppTest {

	@Test
	def testOk = {
		
	}

	@Test
	def printSpecs  {

		(new ModelSpec).reportSpecs
		//(new IDGeneratorSpec).reportSpecs
		// (new GenreCRUDSpec).reportSpecs		
		// (new BookCRUDSpec).reportSpecs
		(new AuthorCRUDSpec).reportSpecs						
	}
	

}


