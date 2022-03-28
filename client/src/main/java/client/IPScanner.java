package client;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class IPScanner {
    private static Future<Boolean> portIsOpen(final ExecutorService es, final String ip, final int port, final int timeout) {
        return es.submit(() -> {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(ip, port), timeout);
                socket.close();
                return true;
            } catch (Exception ex) {
                return false;
            }
        });
    }

    public static List<String> scanServers() {
        List<String> ipList = new ArrayList<>();
        double startTime = System.currentTimeMillis();
        final ExecutorService es = Executors.newFixedThreadPool(16384);
        //final ExecutorService es = Executors.newCachedThreadPool();
        try {
            String ip = "192.168.";
            final List<Future<Boolean>> futures = new ArrayList<>();

            for (int j = 0; j < 256; j++) {
                for (int i = 0; i < 256; i++) {
                    futures.add(portIsOpen(es, ip + j + "." + i, 8080, 5));
                }
            }
            es.shutdown();

            for (int i = 0; i < futures.size(); i++) {
                Future<Boolean> f = futures.get(i);
                if (f.get()) {
                    System.out.println(i / 256 + ", " + i % 256);
                    ipList.add(ip + i / 256 + '.' + i % 256);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println((System.currentTimeMillis() - startTime) / 1000 + " seconds passed");
        return ipList;
    }
}
