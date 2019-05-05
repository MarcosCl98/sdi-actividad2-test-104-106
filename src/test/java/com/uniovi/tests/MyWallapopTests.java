package com.uniovi.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.uniovi.tests.pageobjects.PO_AddBidView;
import com.uniovi.tests.pageobjects.PO_ChatView;
import com.uniovi.tests.pageobjects.PO_ForbiddenView;
import com.uniovi.tests.pageobjects.PO_HomeView;
import com.uniovi.tests.pageobjects.PO_ListBidView;
import com.uniovi.tests.pageobjects.PO_LoginView;
import com.uniovi.tests.pageobjects.PO_NavView;
import com.uniovi.tests.pageobjects.PO_OfertasView;
import com.uniovi.tests.pageobjects.PO_Properties;
import com.uniovi.tests.pageobjects.PO_RegisterView;
import com.uniovi.tests.pageobjects.PO_UsersView;
import com.uniovi.tests.pageobjects.PO_View;
import com.uniovi.tests.util.SeleniumUtils;

//Ordenamos las pruebas por el nombre del método
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MyWallapopTests {
//	@Autowired
//	private UsersService usersService;
//	@Autowired
//	private RolesService rolesService;
//	@Autowired
//	private BidsService bidService;
//	@Autowired
//	private ConversationService conversationService;
//	@Autowired
//	private UsersRepository usersRepository;

	// En Windows (Debe ser la versión 65.0.1 y desactivar las actualizacioens
	// automáticas)):
	static String PathFirefox65 = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
	static String Geckdriver024 = "C:\\Users\\marco\\OneDrive\\Escritorio\\PL-SDI-Sesio╠ün5-material\\geckodriver024win64.exe";

	// Común a Windows y a MACOSX
	static WebDriver driver = getDriver(PathFirefox65, Geckdriver024);
	static String URLremota = "http://ec2-18-217-40-214.us-east-2.compute.amazonaws.com";
	static String URLlocal = "http://localhost:8081";
	static String URL = URLlocal; // Se va a probar con la URL local
	// static String URL = URLremota; // Se va a probar con la URL remota

	public static WebDriver getDriver(String PathFirefox, String Geckdriver) {
		System.setProperty("webdriver.firefox.bin", PathFirefox);
		System.setProperty("webdriver.gecko.driver", Geckdriver);
		WebDriver driver = new FirefoxDriver();
		return driver;
	}

	// Antes de cada prueba se navega al URL home de la aplicaciónn
	@Before
	public void setUp() {
		try {
			driver.navigate().to("localhost:8081/home");
		} catch (WebDriverException ignore) {
			
		}

		driver.navigate().to(URL);
	}

	@Test
	// Registro de Usuario con datos válidos.
	public void Prueba01() {
		// Vamos al formulario de registro
		PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "pruuebaa1@gmail.aaacom", "Prueba", "Antonio", "77777", "77777");
		// Comprobamos que entramos en la sección privada del usuario mirando salgo
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'user-menu')]/a");
		elementos.get(0).click();
		// Comprobamos que está el texto del saldo al tener role user
		PO_View.checkElement(driver, "text", "100");
		
	}


	@Test
	// Registro de Usuario con repetición contraseña inválida.
	public void Prueba02() {
		// Vamos al formulario de registro
		PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "prueba3@gmail.com", "Lorenzo", "Berto", "77778", "77777");
		PO_View.getP();
		// COmprobamos el error de campo vacio.
		PO_View.checkElement(driver, "text", "Error, las contraseñas no coinciden.");
		
	}

	@Test
	// Registro de Usuario con email existente.
	public void Prueba03() {
		// Vamos al formulario de registro
		PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "pedro@gmail.com", "Lorenzo", "Berto", "77777", "77777");
		PO_View.getP();
		// COmprobamos el error de campo vacio.
		PO_View.checkElement(driver, "text", "Error al registrar usuario, email ya existente.");
		
	}

	@Test
	// Iniciar correctamente 
	public void Prueba04() {
		// Logueamos como usuario
		PO_LoginView.log(driver, "martin@gmail.com", "12345");
		// Comprobamos que está el texto de gestion de usuarios que solo posee el admin
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'user-menu')]/a");
		// Comprobamos que está el email
		assertTrue(elementos.get(0).getText().equals("martin@gmail.com"));
		
	}

	@Test
	// Inicio fallido email existente contraseña incorrecta
	public void Prueba05() {
		// Logueamos como usuario
		PO_LoginView.log(driver, "martin@gmail.com", "123456");
		//Comrpobamos error
		PO_View.checkElement(driver, "text", "Email o password incorrecto.");

	}
	@Test
	// Inicio fallido campos vacios
	public void Prueba06() {
		// Logueamos como usuario
		PO_LoginView.log(driver, "", "123456");
		//Comrpobamos error
		PO_View.checkElement(driver, "text", "Error, campo email vacío.");

	}

	@Test
	// Inicio fallido email no existente
	public void Prueba07() {
		// Logueamos como usuario
		PO_LoginView.log(driver, "martin123@gmail.com", "123456");
		//Comrpobamos error
		PO_View.checkElement(driver, "text", "Email o password incorrecto.");

	}


	@Test
	// Comprobar el log out
	public void Prueba08() {
		// Logueamos
		PO_LoginView.log(driver, "martin@gmail.com", "12345");
		// Click en el email
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'user-menu')]/a");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'logout')]");
		// Pinchamos en desconectar.
		elementos.get(0).click();
		// Comprobamos pagina login
		PO_LoginView.checkPOLoginView(driver, PO_Properties.getSPANISH());

	}

	@Test
	// Comprobar que el boton de cerrar sesion no esta si no nos hemos autentificado
	public void Prueba09() {
		// Comprobamos que no está la opcion de desconectarse sin autentificacion
		SeleniumUtils.textoPresentePagina(driver,"Bienvenido a la aplicacion MyWallapop");
		
		//Comprobamos que no está el texto de cerrar sesion
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//*[contains(@href,'/login')]");
		// Comprobamos que está el email
		assertFalse(elementos.get(0).getText().equals("Desconectarse"));
	}

	@Test
	// Mostrar el listado de usuarios y comprobar que se muestran todos los que
	// existen en el sistema
	public void Prueba10() {
		// Logueamos
		PO_LoginView.log(driver, "admin@email.com", "admin");
		// Click en gestion de usuarios
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'users-menu')]/a");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/user/list')]");
		// Pinchamos en ver usuarios
		elementos.get(0).click();
		// Como hay 6 usuarios registrados comprobamso que salgan todos
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		
		assertTrue(elementos.size() == 4);

	}

	@Test
	// Ir a la lista de usuarios, borrar el primer usuarios de la lista y comprobar
	// que se actualiza y dicho usuario no aparece
	public void Prueba11() {
		// Logueamos
		PO_LoginView.log(driver, "admin@email.com", "admin");
		// Click en gestion de usuarios
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'users-menu')]/a");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/user/list')]");
		// Pinchamos en ver usuarios
		elementos.get(0).click();
		// Cargamos los usuarios y vemos que estan todos
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 4);
		// Cogemos el nombre del que se va a borrar para su posterior comprobacion
		String emailYnombre = elementos.get(0).getText();
		String parts[] = emailYnombre.split(" ");
		String nombre = parts[1];
		// Le damos al checkbox
		elementos = PO_View.checkElement(driver, "free", "//input");
		elementos.get(0).click();
		// Borramos
		elementos = PO_View.checkElement(driver, "text", "Borrar");
		// Click a borrar
		elementos.get(0).click();
		// Comprobamos que el usuario no esta
		SeleniumUtils.textoNoPresentePagina(driver, nombre);
		// Y que la lista es 1 menos
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 3);
	}

	@Test
	// Ir a la lista de usuarios, borrar el primer usuarios de la lista y comprobar
	// que se actualiza y dicho usuario no aparece
	public void Prueba12() {
		// Logueamos
		PO_LoginView.log(driver, "admin@email.com", "admin");
		// Click en gestion de usuarios
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'users-menu')]/a");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/user/list')]");
		// Pinchamos en ver usuarios
		elementos.get(0).click();
		// Cargamos los usuarios y vemos que estan todos
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 4);
		// Cogemos el nombre del que se va a borrar para su posterior comprobacion
		// OJO!!!! COGEMOS EL PENULTIMO QUE EL ADMIN NO SE PUEDE BORRAR A SI MISMO
		String emailYnombre = elementos.get(elementos.size() - 2).getText();
		String parts[] = emailYnombre.split(" ");
		String nombre = parts[1];
		// Le damos al checkbox
		elementos = PO_View.checkElement(driver, "free", "//input");
		elementos.get(elementos.size() - 2).click();
		elementos = PO_View.checkElement(driver, "text", "Borrar");
		// Click a borrar
		elementos.get(0).click();
		// Comprobamos que el usuario no esta
		SeleniumUtils.textoNoPresentePagina(driver, nombre);
		// Y que la lista es 1 menos
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 3);
	}

	@Test
	// Ir a la lista de usuarios, borrar 3 usuarios y comprobar que se actualiza y
	// dichos usuarios no aparecen
	public void Prueba13() {
		// Logueamos
		PO_LoginView.log(driver, "admin@email.com", "admin");
		// Click en gestion de usuarios
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'users-menu')]/a");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/user/list')]");
		// Pinchamos en ver usuarios
		elementos.get(0).click();
		// Cargamos los usuarios y vemos que estan todos
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 4);
		// Cogemos el nombre del primer Usuario
		String emailYnombre1 = elementos.get(0).getText();
		String parts1[] = emailYnombre1.split(" ");
		String nombre1 = parts1[1];
		// Cogemos el nombre del segundo Usuario
		String emailYnombre2 = elementos.get(1).getText();
		String parts2[] = emailYnombre2.split(" ");
		String nombre2 = parts2[1];
		// Cogemos el nombre del tercer Usuario
		String emailYnombre3 = elementos.get(2).getText();
		String parts3[] = emailYnombre3.split(" ");
		String nombre3 = parts3[1];

		// Le damos a los checkboxs
		elementos = PO_View.checkElement(driver, "free", "//input");
		elementos.get(0).click();
		elementos.get(1).click();
		elementos.get(2).click();

		// Click a borrar
		By boton = By.className("btn");
		driver.findElement(boton).click();

		// Comprobamos que los usuarios no estan
		SeleniumUtils.textoNoPresentePagina(driver, nombre1);
		SeleniumUtils.textoNoPresentePagina(driver, nombre2);
		SeleniumUtils.textoNoPresentePagina(driver, nombre3);
		// Y que la lista es 3 menos
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 1);
	}

	@Test
	// Dar de alta una nueva oferta y comprobarla
	public void Prueba14() {
		// Logueamos
		PO_LoginView.log(driver, "pedro@gmail.com", "12345");
		// Click en ofertas
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'bids-menu')]/a");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/bid/add')]");
		// Pinchamos añadir oferta
		elementos.get(0).click();
		// Rellenamos el formulario.
		PO_AddBidView.fillForm(driver, "Prueba", "Descricpion", "10");
		// Comprobamos que este
		PO_View.checkElement(driver, "text", "Prueba");
	}

	@Test
	// Dar de alta una nueva oferta con el titulo vacio y comprobar error.
	public void Prueba15() {
		// Logueamos
		PO_LoginView.log(driver, "pedro@gmail.com", "12345");
		// Click en ofertas
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'bids-menu')]");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/bid/add')]");
		// Pinchamos añadir oferta
		elementos.get(0).click();
		// Rellenamos el formulario.
		PO_AddBidView.fillForm(driver, "", "Descricpion", "10");
		// Comprobamos que este
		SeleniumUtils.textoPresentePagina(driver,"Los campos no pueden estar vacios.");
	}

	@Test
	// Ir al listado de ofertas del usuario y comprobar que se muestran todas sus
	// ofertas
	public void Prueba16() {
		// Logueamos
		PO_LoginView.log(driver, "pedro@gmail.com", "12345");
		// Click en ofertas
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'bids-menu')]");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/bid/mybids')]");
		// Pinchamos en ver mis ofertas
		elementos.get(0).click();
		// Como sabemos que tiene 5 ofertas miramos que sean 5
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 3);
	}

	@Test
	// Ir al listado de ofertas del usuario y borrar la primera comprobandolo
	public void Prueba17() {
		// Logueamos
		PO_LoginView.log(driver, "pedro@gmail.com", "12345");
		// Click en ofertas
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'bids-menu')]");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/bid/mybids')]");
		// Pinchamos en ver mis ofertas
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//tr[contains(@id, 'ofertas')]");
		String datos = elementos.get(0).getText();
		String parts[] = datos.split(" ");
		String nombre = parts[0] + " " + parts[1];
		// Borramos la oferta
		elementos = PO_View.checkElement(driver, "free", "//button[contains(@id, 'boton')]");
		elementos.get(0).click();
		// Comprobamos que no esta
		SeleniumUtils.textoNoPresentePagina(driver, nombre);
	}

	@Test
	// Ir al listado de ofertas del usuario y borrar la ultima comprobandolo
	public void Prueba18() {
		// Logueamos
		PO_LoginView.log(driver, "pedro@gmail.com", "12345");
		// Click en ofertas
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'bids-menu')]");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/bid/mybids')]");
		// Pinchamos en ver mis ofertas
		elementos.get(0).click();
		// Cogemos el nombre de la primera oferta para luego comprobarlo
		elementos = PO_View.checkElement(driver, "free", "//tr[contains(@id, 'ofertas')]");
		String datos = elementos.get(elementos.size() - 1).getText();
		String parts[] = datos.split(" ");
		String nombre = parts[0] + " " + parts[1];
		// Borramos la oferta
		elementos = PO_View.checkElement(driver, "free", "//button[contains(@id, 'boton')]");
		elementos.get(elementos.size() - 1).click();
		// Comprobamos que no esta
		SeleniumUtils.textoNoPresentePagina(driver, nombre);

	}

	@Test
	// Hacer una busqueda de las ofertas con el campo vacio y
	// comprobar que se muestra la pagina que corresponde con el listado de paginas
	// existentes
	public void Prueba19() {
		// Logueamos
		PO_LoginView.log(driver, "pedro@gmail.com", "12345");
		// Click en ofertas
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'bids-menu')]");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/bid/list')]");
		// Pinchamos en ver mis ofertas
		elementos.get(0).click();
		// Cogemos el nombre de las ofertas de la primera pagina
		elementos = PO_View.checkElement(driver, "free", "//tr[contains(@id, 'ofertas')]");
		String nombres[] = new String[8];
		for (int i = 0; i < elementos.size(); i++) {
			String datos = elementos.get(i).getText();
			String parts[] = datos.split(" ");
			nombres[i] = parts[0] + " " + parts[1];
		}

		// Clickamos el buscador y buscamos vacio
		elementos = PO_View.checkElement(driver, "free", "//input");

		elementos.get(0).click();
		PO_ListBidView.fillForm(driver, "");
		// Comprobamos la pagina que está igual
		elementos = PO_View.checkElement(driver, "free", "//tr[contains(@id, 'ofertas')]");
		for (int i = 0; i < elementos.size(); i++) {
			PO_View.checkElement(driver, "text", nombres[i]);
		}

	}

	@Test
	// Hacer una busqueda de las ofertas con el campo algo que no aparece
	// comprobar que se muestra la pagina vacia
	public void Prueba20() {
		// Logueamos
		PO_LoginView.log(driver, "pedro@gmail.com", "12345");
		// Click en ofertas
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'bids-menu')]");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/bid/list')]");
		// Pinchamos en ver mis ofertas
		elementos.get(0).click();
		// Cogemos el nombre de las ofertas de la primera pagina
		elementos = PO_View.checkElement(driver, "free", "//tr[contains(@id, 'ofertas')]");
		String nombres[] = new String[8];
		for (int i = 0; i < elementos.size(); i++) {
			String datos = elementos.get(i).getText();
			String parts[] = datos.split(" ");
			nombres[i] = parts[0] + " " + parts[1];
		}
		// Clickamos el buscador y buscamos algo que no existe
		elementos = PO_View.checkElement(driver, "free", "//input");
		elementos.get(0).click();
		PO_ListBidView.fillForm(driver, "zzzzzzz");
		// Comprobamos que esta vacia mirando a veri salen las ofertas que salian antes
		SeleniumUtils.textoNoPresentePagina(driver, nombres[0]);
		SeleniumUtils.textoNoPresentePagina(driver, nombres[1]);
		SeleniumUtils.textoNoPresentePagina(driver, nombres[2]);
		SeleniumUtils.textoNoPresentePagina(driver, nombres[3]);
		SeleniumUtils.textoNoPresentePagina(driver, nombres[4]);
		SeleniumUtils.textoNoPresentePagina(driver, nombres[5]);
		SeleniumUtils.textoNoPresentePagina(driver, nombres[6]);

	}
	
	@Test
	// Hacer una busqueda de las ofertas con el campo en minuscula o mayuscula
	// comprobar que se muestran las correctas
	public void Prueba21() {
		// Logueamos
		PO_LoginView.log(driver, "pedro@gmail.com", "12345");
		// Click en ofertas
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'bids-menu')]");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/bid/list')]");
		// Pinchamos en ver mis ofertas
		elementos.get(0).click();
		// Cogemos el nombre de las ofertas de la primera pagina
		elementos = PO_View.checkElement(driver, "free", "//tr[contains(@id, 'ofertas')]");
		String nombres[] = new String[8];
		for (int i = 0; i < elementos.size(); i++) {
			String datos = elementos.get(i).getText();
			String parts[] = datos.split(" ");
			nombres[i] = parts[0] + " " + parts[1];
		}

		// Clickamos el buscador y buscamos vacio
		elementos = PO_View.checkElement(driver, "free", "//input");

		elementos.get(0).click();
		PO_ListBidView.fillForm(driver, "oferta");
		// Comprobamos la pagina que está igual
		elementos = PO_View.checkElement(driver, "free", "//tr[contains(@id, 'ofertas')]");
		for (int i = 0; i < elementos.size(); i++) {
			PO_View.checkElement(driver, "text", nombres[i]);
		}

	}

	@Test
	// Comprar y ver que se actualiza correctamente el saldo
	public void Prueba22() {
		// Logueamos
		PO_LoginView.log(driver, "pedro@gmail.com", "12345");
		// Click en ofertas
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'bids-menu')]");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/bid/list')]");
		// Pinchamos en ver ofertas
		elementos.get(0).click();
		// Cogemos el dinero que tiene
		int saldoActual = 80;
		// Clickamos el buscador y buscamos una oferta
		elementos = PO_View.checkElement(driver, "free", "//input");
		elementos.get(0).click();
		PO_ListBidView.fillForm(driver, "Oferta 1 Martin");
		// El saldo final tendria que ser el saldo -20 los del precio.
		int saldoFinal = saldoActual - 20;
		// Compramos
		elementos = PO_View.checkElement(driver, "free", "//form[contains(@id, 'botonBuy')]");
		elementos.get(0).click();
		// Miramos saldo
		elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'user-menu')]/a");
		elementos.get(0).click();
		// Comprbamos que el saldo sea la resta
		PO_View.checkElement(driver, "text", String.valueOf(saldoFinal));

	}
	
	@Test
	// Comprar y ver que se actualiza correctamente el saldo a 0
		public void Prueba23() {
			// Logueamos
			PO_LoginView.log(driver, "pedro@gmail.com", "12345");
			// Click en ofertas
			List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'bids-menu')]");
			elementos.get(0).click();
			elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/bid/list')]");
			// Pinchamos en ver ofertas
			elementos.get(0).click();
			// Cogemos el dinero que tiene
			int saldoActual = 80;
			// Clickamos el buscador y buscamos una oferta
			elementos = PO_View.checkElement(driver, "free", "//input");
			elementos.get(0).click();
			PO_ListBidView.fillForm(driver, "Oferta 2 Martin");
			
			// Compramos
			elementos = PO_View.checkElement(driver, "free", "//form[contains(@id, 'botonBuy')]");
			elementos.get(0).click();
			// Miramos saldo
			elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'user-menu')]/a");
			elementos.get(0).click();
			// Comprbamos que el saldo sea la resta
			
			PO_View.checkElement(driver, "text", "0");

		}
	
	@Test
	// Comprar algo q cueste mas del dinero que tenemos
		public void Prueba24() {
			// Logueamos
			PO_LoginView.log(driver, "pedro@gmail.com", "12345");
			// Click en ofertas
			List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'bids-menu')]");
			elementos.get(0).click();
			elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/bid/list')]");
			// Pinchamos en ver ofertas
			elementos.get(0).click();
			// Cogemos el dinero que tiene
			int saldoActual = 80;
			// Clickamos el buscador y buscamos una oferta
			elementos = PO_View.checkElement(driver, "free", "//input");
			elementos.get(0).click();
			PO_ListBidView.fillForm(driver, "Oferta 2 Laura");
			
			// Compramos
			elementos = PO_View.checkElement(driver, "free", "//form[contains(@id, 'botonBuy')]");
			elementos.get(0).click();
			
					
			//Miramos mensaje
			SeleniumUtils.textoPresentePagina(driver, "No tienes dinero suficiente.");
			// Miramos saldo
			elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'user-menu')]/a");
			elementos.get(0).click();
			
			PO_View.checkElement(driver, "text", "80");

		}



	@Test
	// Comprobar que salen las ofertas compradas correctamente
	public void Prueba25() {
		// Logueamos
		PO_LoginView.log(driver, "pedro@gmail.com", "12345");
		// Click en ofertas
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'bids-menu')]");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/bid/list')]");
		// Pinchamos en ver ofertas
		elementos.get(0).click();
		// Clickamos el buscador y buscamos una oferta
		elementos = PO_View.checkElement(driver, "free", "//input");
		elementos.get(0).click();
		PO_ListBidView.fillForm(driver, "Oferta 1 Martin");
		// Compramos
		elementos = PO_View.checkElement(driver, "free", "//form[contains(@id, 'botonBuy')]");
		elementos.get(0).click();
		// Click en ofertas
		elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'bids-menu')]/a");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/bid/mybuyedbids')]");
		// Pinchamos en ver ofertas compradas
		elementos.get(0).click();
		// Este usuario tiene dos ofertas compradas, lo comprobamos:
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 1);
	}





	@Test
	// Al crear una oferta marcar dicha oferta como destacada y a continuación
	// comprobar: i) que
	// aparece en el listado de ofertas destacadas para los usuarios y que el saldo
	// del usuario se actualiza
	// adecuadamente en la vista del ofertante (-20).
	public void Prueba26() {
		// Logueamos
		PO_LoginView.log(driver, "pedro@gmail.com", "12345");
		// Click en ofertas
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'bids-menu')]");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/bid/add')]");
		// Pinchamos añadir oferta
		elementos.get(0).click();
		// Rellenamos el formulario.
		PO_AddBidView.fillFormOustanding(driver, "Prueba", "Descricpion", "10");
		// Vamos al home
		driver.navigate().to("http://localhost:8081/");
		// Comprobamos que este
		PO_View.checkElement(driver, "text", "Prueba");
		// Miramos q el saldo sea 100-20=80
		elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'user-menu')]/a");
		elementos.get(0).click();
		PO_View.checkElement(driver, "text", "60");
	}

	@Test
	// Sobre el listado de ofertas de un usuario con menos de 20 euros de saldo,
	// pinchar en el
	// enlace Destacada y a continuación comprobar: i) que aparece en el listado de
	// ofertas destacadas para los
	// usuarios y que el saldo del usuario se actualiza adecuadamente en la vista
	// del ofertante (-20).
	public void Prueba27() {
		// Logueamos
		PO_LoginView.log(driver, "pedro@gmail.com", "12345");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'bids-menu')]");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/bid/mybids')]");
		// Pinchamos en ver mis ofertas
		elementos.get(0).click();
		// Cogemos e intentamos destacar
		elementos = PO_View.checkElement(driver, "text", "Destacar (20€)");
		// Pinchamos
		elementos.get(1).click();
		// Vamos al home
		driver.navigate().to("http://localhost:8081/");
		// Comprobamos que esté la oferta
		SeleniumUtils.textoPresentePagina(driver, "Oferta 2 Pedro");
		// Y miramos que el salgo ha bajado 20, es decir 100-20 = 80.
		elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'user-menu')]/a");
		elementos.get(0).click();
		PO_View.checkElement(driver, "text", "60");

	}

	@Test
	// Sobre el listado de ofertas de un usuario con menos de 20 euros de saldo,
	// pinchar en el
	// enlace Destacada y a continuación comprobar que se muestra el mensaje de
	// saldo no suficiente.
	public void Prueba28() {
		// Logueamos
		PO_LoginView.log(driver, "pedro@gmail.com", "12345");
		// Click en ofertas
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'bids-menu')]");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/bid/list')]");
		// Pinchamos en ver ofertas
		elementos.get(0).click();
		
		// Clickamos el buscador y buscamos una oferta
		elementos = PO_View.checkElement(driver, "free", "//input");
		elementos.get(0).click();
		PO_ListBidView.fillForm(driver, "Oferta 2 Martin");
		
		// Compramos
		elementos = PO_View.checkElement(driver, "free", "//form[contains(@id, 'botonBuy')]");
		elementos.get(0).click();
		// Miramos saldo
		elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'user-menu')]/a");
		elementos.get(0).click();
		// Comprbamos que el saldo sea la resta
		
		PO_View.checkElement(driver, "text", "0");
		elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'bids-menu')]");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, '/bid/mybids')]");
		// Pinchamos en ver mis ofertas
		elementos.get(0).click();
		// Cogemos e intentamos destacar
		elementos = PO_View.checkElement(driver, "text", "Destacar (20€)");
		elementos.get(0).click();
		PO_View.checkElement(driver, "text", "No tienes 20€ o mas para poder destacar la oferta.");

	}
	
	@Test
	//Inicio valido
	public void Prueba29() {
		driver.navigate().to("http://localhost:8081/cliente.html");
		// Logueamos
		PO_LoginView.logClient(driver, "pedro@gmail.com", "12345");
		//Comprobamos que estamos en la lista ofertas
		PO_View.checkElement(driver, "text", "Ofertas");
	}
	
	@Test
	//Inicio invalido pass incorrecta
	public void Prueba30() {
		driver.navigate().to("http://localhost:8081/cliente.html");
		// Logueamos
		PO_LoginView.logClient(driver, "pedro@gmail.com", "1234533");
		//Comprobamos que estamos en la lista ofertas
		PO_View.checkElement(driver, "text", "Usuario no encontrado o campos inválidos.");
	}
	
	@Test
	//Inicio invalido campo vacio
	public void Prueba31() {
		driver.navigate().to("http://localhost:8081/cliente.html");
		// Logueamos
		PO_LoginView.logClient(driver, "pedro@gmail.com", "");
		//Comprobamos que estamos en la lista ofertas
		PO_View.checkElement(driver, "text", "Usuario no encontrado o campos inválidos.");
	}
	
	@Test
	//Comprobar que se muestran las ofertas disponibles menos las del usuario
	public void Prueba32() {
		driver.navigate().to("http://localhost:8081/cliente.html");
		// Logueamos
		PO_LoginView.logClient(driver, "pedro@gmail.com", "12345");
		//Comprobamos que estamos en la lista ofertas
		PO_View.checkElement(driver, "text", "Ofertas");
		//Comprobamos que están las ofertas de los demas usuarios
		PO_View.checkElement(driver, "text", "Oferta 1 Martin");
		PO_View.checkElement(driver, "text", "Oferta 2 Martin");
		PO_View.checkElement(driver, "text", "Oferta 1 Laura");
		PO_View.checkElement(driver, "text", "Oferta 2 Laura");
		
	}
	
	
	@Test
	//Comprobar que se muestran las ofertas disponibles menos las del usuario
	public void Prueba33() {
		driver.navigate().to("http://localhost:8081/cliente.html");
		// Logueamos
		PO_LoginView.logClient(driver, "pedro@gmail.com", "12345");
		//Comprobamos que estamos en la lista ofertas
		PO_View.checkElement(driver, "text", "Ofertas");
		//Comprobamos que están las ofertas de los demas usuarios
		List<WebElement> elementos = PO_View.checkElement(driver, "text", "Enviar mensaje");
		elementos.get(0).click();
		//Mandamos mensaje
		// Clickamos el buscador y buscamos una oferta
		elementos = PO_View.checkElement(driver, "free", "//input");
		elementos.get(0).click();
		PO_ListBidView.fillForm(driver, "Hola");
		elementos= PO_View.checkElement(driver, "text", "Enviar mensaje");
		elementos.get(0).click();
		
		
	}
	
	@Test
	//Mandar mensaje en el chat
	public void Prueba34() {
		driver.navigate().to("http://localhost:8081/cliente.html");
		// Logueamos
		PO_LoginView.logClient(driver, "pedro@gmail.com", "12345");
		//Vamos a las conversaciones
		SeleniumUtils.esperarSegundos(driver, 3);
		driver.navigate().to("http://localhost:8081/cliente.html?w=conversations");
		//Abrimos chat
		SeleniumUtils.esperarSegundos(driver, 3);
		List<WebElement> elementos = PO_View.checkElement(driver, "text", "Abrir");
		elementos.get(0).click();
		//Mandamos mensaje
		// Clickamos el buscador y buscamos una oferta
		elementos = PO_View.checkElement(driver, "free", "//input");
		elementos.get(0).click();
		SeleniumUtils.esperarSegundos(driver, 3);
		PO_ListBidView.fillForm(driver, "Hola");
		elementos= PO_View.checkElement(driver, "text", "Enviar mensaje");
		elementos.get(0).click();
		SeleniumUtils.esperarSegundos(driver, 3);
		elementos= PO_View.checkElement(driver, "text", "Hola");
	}
	
	@Test
	//Mostrar listado de conversaciones 
	public void Prueba35() {
		driver.navigate().to("http://localhost:8081/cliente.html");
		// Logueamos
		PO_LoginView.logClient(driver, "pedro@gmail.com", "12345");
		//Vamos a las conversaciones
		SeleniumUtils.esperarSegundos(driver, 3);
		driver.navigate().to("http://localhost:8081/cliente.html?w=conversations");
		//Comprobamos que hay el numero de chat que deberia
		SeleniumUtils.esperarSegundos(driver, 3);
		List<WebElement> elementos = PO_View.checkElement(driver, "text", "Abrir");
		
		assertTrue(elementos.size() == 3);
	}
	
	@Test
	//Eliminar primer chat
	public void Prueba36() {
		driver.navigate().to("http://localhost:8081/cliente.html");
		// Logueamos
		PO_LoginView.logClient(driver, "pedro@gmail.com", "12345");
		//Vamos a las conversaciones
		SeleniumUtils.esperarSegundos(driver, 3);
		driver.navigate().to("http://localhost:8081/cliente.html?w=conversations");
		//Comprobamos que hay el numero de chat que deberia
		SeleniumUtils.esperarSegundos(driver, 3);
		List<WebElement> elementos = PO_View.checkElement(driver, "text", "Eliminar");
		//Comprobamos la existencia del chat con martin
		elementos= PO_View.checkElement(driver, "text", "martin@gmail.com");
		assertTrue(elementos.size()==2);
		elementos = PO_View.checkElement(driver, "text", "Eliminar");
		//Eliminamos el chat
		elementos.get(0).click();
		SeleniumUtils.esperarSegundos(driver, 3);
		elementos= PO_View.checkElement(driver, "text", "martin@gmail.com");
		assertTrue(elementos.size()==1);
		
	}
	
	@Test
	//Eliminar ultimo chat
	public void Prueba37() {
		driver.navigate().to("http://localhost:8081/cliente.html");
		// Logueamos
		PO_LoginView.logClient(driver, "pedro@gmail.com", "12345");
		//Vamos a las conversaciones
		SeleniumUtils.esperarSegundos(driver, 3);
		driver.navigate().to("http://localhost:8081/cliente.html?w=conversations");
		//Comprobamos que hay el numero de chat que deberia
		SeleniumUtils.esperarSegundos(driver, 3);
		List<WebElement> elementos = PO_View.checkElement(driver, "text", "Eliminar");
		//Comprobamos la existencia del chat con martin
		elementos= PO_View.checkElement(driver, "text", "martin@gmail.com");
		assertTrue(elementos.size()==2);
		elementos = PO_View.checkElement(driver, "text", "Eliminar");
		//Eliminamos el chat
		elementos.get(1).click();
		SeleniumUtils.esperarSegundos(driver, 3);
		elementos= PO_View.checkElement(driver, "text", "martin@gmail.com");
		assertTrue(elementos.size()==1);
		
	}

	// Después de cada prueba se borran las cookies del navegador
	@After
	public void tearDown() {
		driver.manage().deleteAllCookies();
	}

	// Antes de la primera prueba
	@BeforeClass
	static public void begin() {
	}

	// Al finalizar la última prueba
	@AfterClass
	static public void end() {
		// Cerramos el navegador al finalizar las pruebas
		driver.quit();
	}

}