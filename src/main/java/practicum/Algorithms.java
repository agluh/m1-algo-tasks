package practicum;

import java.util.*;
import java.util.stream.Collectors;

public class Algorithms {

    /**
     * Отсортируйте список, НЕ используя методы стандартной библиотеки (напр. Collections.sort).
     */
    public static List<Integer> sort(List<Integer> list) {
        if (list.isEmpty()) {
            return Collections.emptyList();
        }

        int[] array = list.stream().mapToInt(Integer::intValue).toArray();

        // Insertion sort
        for (int i = 1; i < array.length; i++) {
            int tmp = array[i];
            int j = i;
            while (j > 0 && array[j - 1] > tmp) {
                array[j] = array[j - 1];
                j--;
            }
            array[j] = tmp;
        }

        return Arrays.stream(array).boxed().collect(Collectors.toList());
    }

    /**
     * Удалите дубликаты из списка.
     *
     * Усложнение: не используйте дополнительные структуры данных
     *  для хранения промежуточных значений.
     *  (списки, массивы, хэш-таблицы, множества и т.п.).
     * К списку-результату это не относится.
     */
    public static List<Integer> removeDuplicates(List<Integer> list) {
        if (list.isEmpty()) {
            return Collections.emptyList();
        }

        // Way to easy :)
        // return list.stream().distinct().collect(Collectors.toList());

        List<Integer> out = new LinkedList<>(list);

        int endOfDuplicatesFreePartIndex = 0;

        while (endOfDuplicatesFreePartIndex < out.size()) {
            Iterator<Integer> it = out.iterator();

            // Skip all elements on the left because we've already checked them
            int index = 0;
            while (index < endOfDuplicatesFreePartIndex) {
                it.next();
                index++;
            }

            int candidateForDeduplication = it.next();

            // Search for the rest of list and remove all duplicates
            while (it.hasNext()) {
                int current = it.next();
                if (current == candidateForDeduplication) {
                    it.remove();
                }
            }

            endOfDuplicatesFreePartIndex++;
        }

        return out;
    }

    /**
     * Проверьте, является ли список "палиндромом".
     * Палиндром -- это список, который в обе стороны читается одинаково.
     * Например:
     *  палиндромы: [1 2 1], [3 2 1 2 3], [2 2 2], []
     *  не палиндромы: [1 2 3], [2 2 3], [3 2 1 3 2]
     *
     * Доп. условие: у алгоритма должна быть линейная сложность, O(n)
     */
    public static boolean isPalindrome(List<Integer> list) {
        for (int i = 0; i < list.size() / 2; i++) {
            int mirroredIndex = list.size() - i - 1;
            if (!list.get(i).equals(list.get(mirroredIndex))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Объедините два отсортированных списка в один отсортированный список.
     * Например:
     * [1 3 5] + [2 4 6] = [1 2 3 4 5 6]
     * [1 2 3] + [1 3 5] = [1 1 2 3 3 5]
     * [] + [1] = [1]
     * [7] + [1 4] = [1 4 7]
     *
     * Доп. условие: у алгоритма должна быть линейная сложность, O(n).
     */
    public static List<Integer> mergeSortedLists(List<Integer> a, List<Integer> b) {
        if (a.isEmpty() && b.isEmpty()) {
            return Collections.emptyList();
        }

        if (a.isEmpty()) {
            return b;
        }

        if (b.isEmpty()) {
            return a;
        }

        Iterator<Integer> itA = a.iterator();
        Iterator<Integer> itB = b.iterator();
        List<Integer> out = new ArrayList<>(a.size() + b.size());

        int i = itA.next();
        int j = itB.next();

        while (true) {
            if (i <= j) {
                out.add(i);

                if (itA.hasNext()) {
                    i = itA.next();
                } else {
                    out.add(j);
                    break;
                }
            } else {
                out.add(j);

                if (itB.hasNext()) {
                    j = itB.next();
                } else {
                    out.add(i);
                    break;
                }
            }
        }

        // Now append all remaining elements (one of the list may still have elements)
        while (itA.hasNext()) {
            out.add(itA.next());
        }

        while (itB.hasNext()) {
            out.add(itB.next());
        }

        return out;
    }

    /**
     * Проверьте, что в массиве нет дубликатов.
     * Верните true, если дубликатов нет, иначе false.
     *
     * Усложнение: не используйте дополнительные структуры данных
     *  (списки, массивы, хэш-таблицы, множества и т.п.).
     */
    public static boolean containsEveryElementOnce(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = i + 1; j < array.length; j++) {
                if (array[i] == array[j]) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Определите, является ли один массив перестановкой другого.
     * Т.е. в массивах хранится один и тот же набор элементов, но (возможно) в разном порядке.
     *
     * Для решения нжуно использовать одну хэш-таблицу.
     *
     * Например:
     * [1 2 3] и [3 2 1] = true
     * [1 1 2] и [1 2 1] = true
     * [1 2 3] и [1 2 3] = true
     * [] и [] = true
     *
     * [1 2] и [1 1 2] = false, разный набор элементов
     */
    public static boolean isPermutation(int[] a, int[] b) {
        if (a.length == 0 && b.length == 0 || Arrays.equals(a, b)) {
            return true;
        }

        if (a.length != b.length) {
            return false;
        }

        Map<Integer, Integer> numberCounts = new HashMap<>();
        for (int i : a) {
            numberCounts.merge(i, 1, Integer::sum);
        }

        for (int i : b) {
            if (!numberCounts.containsKey(i)) {
                return false;
            }

            int count = numberCounts.get(i) - 1;

            if (count == 0) {
                numberCounts.remove(i);
            } else {
                numberCounts.put(i, count);
            }
        }

        // If arrays have the same group of numbers we should have empty map.
        return numberCounts.isEmpty();
    }

    /**
     * Сложная задача.
     *
     * В памяти компьютера изображения (часто) хранятся в виде двумерного массива.
     * Напишите метод, который повернёт "изображение" на 90 градусов вправо.
     * "Изображение" в данном примере -- двумерный массив целых чисел.
     *
     * Например:
     * на входе:
     * [ [1 2]
     *   [3 4]
     *   [5 6] ]
     *
     * на выходе:
     * [ [5 3 1]
     *   [6 4 2] ]
     */
    public static int[][] rotateRight(int[][] image) {
        if (image.length == 0) {
            return new int[][]{};
        }

        int h = image.length;
        int w = image[0].length;

        int[][] out = new int[w][h];

        for (int j = 0; j < w; j++) {
            for (int i = 0; i < h; i++) {
                out[j][h - i - 1] = image[i][j];
            }
        }

        return out;
    }
}
