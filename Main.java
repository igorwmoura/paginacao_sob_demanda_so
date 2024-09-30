import static java.lang.String.format;

import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Main {
  public static void main(String[] args) {
    List<Integer> pages = new ArrayList<>();
    try (BufferedReader br =
             new BufferedReader(new FileReader("entrada.txt"))) {
      String line = br.readLine();
      while (line != null) {
        pages.add(Integer.parseInt(line));
        line = br.readLine();
      }
    } catch (IOException e) {
      System.out.println("Erro ao ler arquivo");
    }

    fifo(pages);
    otm(pages);
    lru(pages);
  }

  public static void fifo(List<Integer> pages) {
    List<Integer> memoria = new ArrayList<>(pages);
    int pageAmount = memoria.get(0);
    Queue<Integer> fifo = new LinkedList<>();
    int count = 0;
    memoria.remove(0);

    for (int i : memoria) {
      if (fifo.size() < pageAmount) {
        if (!fifo.contains(i)) {
          fifo.add(i);
          count++;
        }
      } else {
        if (!fifo.contains(i)) {
          fifo.poll();
          fifo.add(i);
          count++;
        }
      }
    }
    System.out.println(String.format("FIFO %d", count));
  }

  public static void otm(List<Integer> pages) {
    List<Integer> memoria = new ArrayList<>(pages);
    int pageAmount = memoria.get(0);
    List<Integer> otm = new ArrayList<>();
    int count = 0;
    memoria.remove(0);

    for (int currentIndex = 0; currentIndex < memoria.size(); currentIndex++) {
      int page = memoria.get(currentIndex);
      if (otm.size() < pageAmount) {
        if (!otm.contains(page)) {
          otm.add(page);
          count++;
        }
      } else {
        if (!otm.contains(page)) {
          int indexToReplace = -1;
          int farthest = currentIndex;
          for (int j = 0; j < otm.size(); j++) {
            int nextUse = Integer.MAX_VALUE;
            for (int k = currentIndex + 1; k < memoria.size(); k++) {
              if (otm.get(j).equals(memoria.get(k))) {
                nextUse = k;
                break;
              }
            }
            if (nextUse > farthest) {
              farthest = nextUse;
              indexToReplace = j;
            }
          }
          if (indexToReplace != -1) {
            otm.set(indexToReplace, page);
            count++;
          }
        }
      }
    }
    System.out.println(String.format("OTM %d", count));
  }

  public static void lru(List<Integer> pages) {
    List<Integer> memoria = new ArrayList<>(pages);
    int pageAmount = memoria.get(0);
    memoria.remove(0);

    LinkedHashSet<Integer> lruSet = new LinkedHashSet<>();
    int count = 0;

    for (int page : memoria) {
      if (!lruSet.contains(page)) {
        count++;

        if (lruSet.size() == pageAmount) {
          int lruPage = lruSet.iterator().next();
          lruSet.remove(lruPage);
        }
      } else {
        lruSet.remove(page);
      }
      lruSet.add(page);
    }

    System.out.println(String.format("LRU %d", count));
  }
}
