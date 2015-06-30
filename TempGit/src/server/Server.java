package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private ServerSocket serverSocket;

	public Server(int port) {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() throws IOException {
		System.out.println("Server Started.");
		while (true) {
			Socket socket = serverSocket.accept();
			Runnable runnable = new Worker(socket);
			Thread thread = new Thread(runnable);
			thread.start();
		}
	}

	public static void main(String[] args) throws IOException {
		// server start
		Server server = new Server(9999);
		server.start();
	}

	public class Worker implements Runnable {
		private Socket socket;

		public Worker(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {

			try (BufferedReader input = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
					PrintStream output = new PrintStream(
							socket.getOutputStream(), true);) {
				String data = input.readLine();
				while (data != null) {
					System.out.println("From Client:" + data + socket);
					output.println("From Server:" + data);
					data = input.readLine();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (socket != null) {
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}
}
