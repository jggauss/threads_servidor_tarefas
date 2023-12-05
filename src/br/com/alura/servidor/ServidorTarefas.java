package br.com.alura.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServidorTarefas {

	public static void main(String[] args) throws Exception {

		System.out.println("--- Iniciando Servidor ---");
		Runtime runtime = Runtime.getRuntime();
		int qtdProcessadores = runtime.availableProcessors();
		System.out.println("Qtd de processadores: " + qtdProcessadores);

		ServerSocket servidor = new ServerSocket(12345);

		ExecutorService threadPool = Executors.newCachedThreadPool();

		Set<Thread> todasAsThreads = Thread.getAllStackTraces().keySet();

		while (true) {
			Socket socket = servidor.accept();
			System.out.println("Aceitando novo cliente na porta :" + socket.getPort());

			for (Thread thread : todasAsThreads) {
				System.out.println(thread.getName());
			}

			DistribuirTarefas distribuirTarefas = new DistribuirTarefas(socket);
			threadPool.execute(distribuirTarefas);
			// Thread threadCliente = new Thread(distribuirTarefas);
			// threadCliente.start();

		}

	}

}
