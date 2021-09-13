package crabster.rudakov.sberschoollesson5hwk;

/**
 * Класс представляет собой шаблон для создания объекта, который хранит координаты
 * каждой ячейки игрового поля и присвоенные им значения статуса (0 - пустая ячейка,
 * 1 - в ячейке крестик, поставленный игроком, 2 - нолик, поставленный AI
 * */
public class SingleCoordinate {

    private final int y;
    private final int x;

    public SingleCoordinate(int y, int x, int image) {
        this.y = y;
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

}
