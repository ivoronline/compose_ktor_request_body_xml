package com.ivoronline.compose_ktor_request_body_xml

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.xml.*
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement

//==================================================================
// MAIN ACTIVITY
//==================================================================
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {

      var responseBody by remember { mutableStateOf("") }
      val coroutineScope = rememberCoroutineScope()

      Button(onClick = { coroutineScope.launch { responseBody = callURL() } }) {
        Text("RESPONSE: $responseBody")
      }

    }
  }
}

//==================================================================
// CALL URL
//==================================================================
suspend fun callURL() : String  {

  //CONFIGURE CLIENT
  val client = HttpClient(CIO) {
    install(ContentNegotiation) { xml() }
  }

  //CAL URL
  val httpResponse: HttpResponse = client.post("http://192.168.0.108:8080/SendBodyXML") {
    contentType(ContentType.Application.Xml)
    setBody(PersonDTOXML(1, "John", 20))
  }

  //CLOSE CLIENT
  client.close()

  //RETURN PERSON
  return httpResponse.body();

}

//==================================================================
// PERSON DTO XML
//==================================================================
@Serializable
data class PersonDTOXML(
                    val id  : Int,     //Serialize Property into XML Property      (default)
  @XmlElement(true) val name: String,  //Serialize Property into XML Child Element
  @XmlElement(true) val age : Int      //Serialize Property into XML Child Element
)
