package crabster.rudakov.sberschoollesson5hwk;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс представляет собой AI, который имеет роль как игрока так и менеджера
 * игрового поля. В нём создаётся map, связывающий View и значения координат и
 * статусов ячеек.
 */
public class ArtificialIntelligence {

    private final int[][] gameField;
    private Map<Integer, SingleCoordinate> map = new HashMap<>();

    public ArtificialIntelligence() {
        this.gameField = new int[][]{{0, 0, 0},
                                     {0, 0, 0},
                                     {0, 0, 0}};
    }

    /**
     * Метод осуществояет связывание ImageView каждой ячейки игрового поля и объектов
     * с координатами и пока что нулевыми значениями в ячейках.
     */
    public void bindFieldsAndViews(int field_00, int field_01, int field_02,
                                   int field_10, int field_11, int field_12,
                                   int field_20, int field_21, int field_22) {
        map.put(field_00, new SingleCoordinate(0, 0, 0));
        map.put(field_01, new SingleCoordinate(0, 1, 0));
        map.put(field_02, new SingleCoordinate(0, 2, 0));
        map.put(field_10, new SingleCoordinate(1, 0, 0));
        map.put(field_11, new SingleCoordinate(1, 1, 0));
        map.put(field_12, new SingleCoordinate(1, 2, 0));
        map.put(field_20, new SingleCoordinate(2, 0, 0));
        map.put(field_21, new SingleCoordinate(2, 1, 0));
        map.put(field_22, new SingleCoordinate(2, 2, 0));
    }

    /**
     * Отмечает на игровом поле ход игрока, создавая объект с заданными координатами
     * и значением статуса, далее кладёт его в map, привязав ко View новое значение.
     */
    public void setPlayersStep(int y, int x) {
        if (gameField[y][x] == 0) gameField[y][x] = 1;
        SingleCoordinate playerStep = new SingleCoordinate(y, x, 1);
        map.put(findAIStep(playerStep), playerStep);
    }

    /**
     * Проверяет наличие свободных ячеек(со значением 0) и возвращает результат проверки.
     */
    public boolean verifySteps() {
        for (int y = 0; y < gameField.length; y++) {
            for (int x = 0; x < gameField.length; x++) {
                if (gameField[y][x] == 0) return true;
            }
        }
        return false;
    }

    /**
     * После проверки возможности хода, рандомно генерирует ответный ход, отмечая его на
     * игровом поле, и создавая объект с заданными координатами и значением 2, далее кладёт
     * его в map, привязав ко View новое значение, возвращания в Activity ID сответствующего
     * View.
     */
    public int setAIStep() {
        while (true) {
            if (!verifySteps()) break;
            int y = (int) (Math.random() * 3);
            int x = (int) (Math.random() * 3);
            if (gameField[y][x] == 0) {
                gameField[y][x] = 2;
                SingleCoordinate aIStep = new SingleCoordinate(y, x, 2);
                int viewId = findAIStep(aIStep);
                map.put(findAIStep(aIStep), aIStep);
                return viewId;
            }
        }
        return 0;
    }

    /**
     * Метод получает из привязочного map ключ, ассоциированный с ID View, в которое AI был
     * сделан ход. Так как координаты вновь созданного объекта известны, то по ним достается
     * ключ.
     */
    private int findAIStep(SingleCoordinate aIStep) {
        int key = 0;
        for (Map.Entry<Integer, SingleCoordinate> pair : map.entrySet()) {
            if (pair.getValue().getY() == aIStep.getY() &&
                    pair.getValue().getX() == aIStep.getX()) {
                key = pair.getKey();
            }
        }
        return key;
    }

    /**
     * Метод проверяет по всем направлениям наличие победителя, результат передает в Активити.
     */
    public boolean checkWin(int image) {
        for (int i = 0; i < 3; i++) {
            if ((gameField[i][0] == image && gameField[i][1] == image && gameField[i][2] == image) ||
                    (gameField[0][i] == image && gameField[1][i] == image && gameField[2][i] == image))
                return true;
        }
        if ((gameField[0][0] == image && gameField[1][1] == image && gameField[2][2] == image) ||
                (gameField[2][0] == image && gameField[1][1] == image && gameField[0][2] == image)) {
            return true;
        }
        return false;
    }

}