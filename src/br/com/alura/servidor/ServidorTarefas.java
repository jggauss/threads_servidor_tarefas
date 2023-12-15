package br.com.alura.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServidorTarefas {

	private ExecutorService threadPool;
	private ServerSocket servidor;
	//private volatile boolean estaRodando;
	private AtomicBoolean estaRodando;
	private BlockingQueue<String> filaComandos;
	
	public ServidorTarefas() throws IOException {
		System.out.println("--- Iniciando Servidor ---");
		this.servidor = new ServerSocket(12345);
		this.threadPool = Executors.newCachedThreadPool(new  FabricaDeThreads());
		this.estaRodando = new AtomicBoolean(true);
		this.filaComandos = new ArrayBlockingQueue<String>(2);
		iniciarConsumidores();

	}
		
	private void iniciarConsumidores() {
		int qtdConsumidores = 2;
		for (int i = 0; i < qtdConsumidores; i++) {
			TarefaConsumir tarefa = new TarefaConsumir(filaComandos);
			this.threadPool.execute(tarefa);
			
		}
		
	}

	public void rodar() throws IOException {
		try {
			while (this.estaRodando.get()) {
				Socket socket = servidor.accept();
				System.out.println("Aceitando novo cliente na porta :" + socket.getPort());
				DistribuirTarefas distribuirTarefas = new DistribuirTarefas(threadPool, filaComandos ,socket,this);
				threadPool.execute(distribuirTarefas);
			}
		} catch (SocketException e) {
			System.out.println("SocketException, Está rodando : "+ this.estaRodando);
		}

	}
	public void parar() throws IOException {
		this.estaRodando.set(false);
		servidor.close();
		threadPool.shutdown();
//		System.exit(0);

	}
	public static void main(String[] args) throws Exception {

		ServidorTarefas servidor = new ServidorTarefas();
		servidor.rodar();
		servidor.parar();
	}


}
