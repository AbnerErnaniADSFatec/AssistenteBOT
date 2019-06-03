import model.*;
import view.*;
import controller.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.io.IOException;
import com.pengrad.telegrambot.*;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
public class Start {
	public static void main(String[] args) throws JSONException, ParseException, IOException{
		boolean executa = false;
		ControllUser controlluser = new ControllUser();
		if ( controlluser.testeCon() ) { executa = true; }
		Calendar date = GregorianCalendar.getInstance();
		EventUser event = new EventUser("","");
		User user = null;
		Note note = null;
		//Criação do objeto Bot com as informações de acesso
		TelegramBot bot = TelegramBotAdapter.build("780392770:AAEu1SsvTJUWd9QysdllyzQfLJvFaXUPNMQ");
		// https://www.getpostman.com/
		//objeto responsável por receber as mensagens
		GetUpdatesResponse updatesResponse;
		//objeto responsável por gerenciar o envio de respostas
		SendResponse sendResponse;
		//objeto responsável por gerenciar o envio de ações do chat
		BaseResponse baseResponse;
		//Controle de off-set, isto é, a partir desse ID  será lido as mensagens pendentes de um off-set (limite inicial)
		int m = 0;
		//loop infinito pode ser alterado por algum timer de intervalo curto
		FileReader reader = new FileReader(
			System.getProperty("user.dir") +
			System.getProperty("file.separator") +
			"response.json"
		); // ("//home//abner//response.json");
		JSONParser parser = new JSONParser();
		JSONObject response = null;
		try{
			response = (JSONObject) parser.parse(reader);
		} catch (FileNotFoundException e) {
            e.printStackTrace();
		}
		while(executa) {
			updatesResponse = bot.execute(new GetUpdates().limit(100).offset(m));
			List<Update> updates = updatesResponse.updates();
			date = new GregorianCalendar(2020,7,20,10,35);
			for( Update update : updates ) {
				m = update.updateId() + 1;
				System.out.println("Recebendo Mensagem: " + update.message().text());
				baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
				System.out.println("Resposta de Chat Action Enviada: " + baseResponse.isOk());
				String resp = null;
				try {
					user = new User(update.message().chat().id(),update.message().text(), new ArrayList<>());
				} catch ( Exception e ) {
					e.printStackTrace();
				}
				try {
					event.setTexto(update.message().text());
				} catch ( Exception e ) {
					e.printStackTrace();
				}
				try {
					event.setAssunto(update.message().text());
				} catch ( Exception e ) {
					e.printStackTrace();
				}
				try {
					date = new GregorianCalendar(
						Integer.parseInt(update.message().text().substring(6,10)),
		    			Integer.parseInt(update.message().text().substring(3,5)),
		    			Integer.parseInt(update.message().text().substring(0,2)),
						Integer.parseInt(update.message().text().substring(11,13)),
						Integer.parseInt(update.message().text().substring(15,17))
					);
				} catch ( Exception e ) {
					e.printStackTrace();
				}
				try {
					if ( (user != null) && (event != null)) {
						note = new Note(user, date, event);
						note.saveNote();
					}
				} catch ( Exception e ) {
					e.printStackTrace();
				}
				if ( !(response.get(update.message().text().toLowerCase()) == null) ) {
					if ( note == null ){
						resp = "Nota Salva!";
					} else {
						resp = response.get(update.message().text().toLowerCase()).toString();
					}
				} else {
					resp = "nao entendi!";
				}
				sendResponse = bot.execute(new SendMessage(update.message().chat().id(), resp));
				System.out.println("Mensagem enviada: " + sendResponse.isOk());
				System.out.println("ChatID: " + update.message().chat().id());
			}
		}
	}
}