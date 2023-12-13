package bsu.rfct.course2.group8.Slavinski.var7;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.geom.*;
import javax.swing.JPanel;
@SuppressWarnings("serial")
public class GraphicsDisplay extends JPanel {

    // Список координат точек для построения графика
    private Double[][] graphicsData;

    // Флаговые переменные, задающие правила отображения графика
    private boolean showAxis = true, showMarkers = true, showLevels = false;

    // Границы диапазона пространства, подлежащего отображению
    private double minX, maxX, minY, maxY;

    // Используемый масштаб отображения
    private double scale;

    // Различные стили черчения линий
    private BasicStroke graphicsStroke, axisStroke, markerStroke, levelStroke;

    // Различные шрифты отображения надписей
    private Font axisFont, levelsFont;

    // Среднее арифметическое значений функций в точках


    public GraphicsDisplay() {

        setBackground(Color.WHITE);

        // Сконструировать необходимые объекты, используемые в рисовании
        // Перо для рисования графика
        graphicsStroke = new BasicStroke(5.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_ROUND, 10.0f, new float[] {5,5,10,5,5,5,20,5,10,5,5,5}, 0.0f);

        // Перо для рисования осей координат
        axisStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);

        // Перо для рисования контуров маркеров
        markerStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);

        // Перо для рисования уровней графика
        levelStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, new float [] {3, 2}, 0.0f);

        // Шрифт для подписей осей координат
        levelsFont = new Font("Serif", Font.PLAIN, 20);
        axisFont = new Font("Serif", Font.BOLD, 36);
    }

    // Данный метод вызывается из обработчика элемента меню "Открыть файл с графиком"
    // главного окна приложения в случае успешной загрузки данных
    public void showGraphics(Double[][] graphicsData) {

        // Сохранить массив точек во внутреннем поле класса
        this.graphicsData = graphicsData;
        // Запросить перерисовку компонента, т.е. неявно вызвать paintComponent()
        repaint();
    }

    // Методы-модификаторы для изменения параметров отображения графика
    // Изменение любого параметра приводит к перерисовке области
    public void setShowAxis(boolean showAxis) {
        this.showAxis = showAxis;
        repaint();
    }
    public void setShowMarkers(boolean showMarkers) {
        this.showMarkers = showMarkers;
        repaint();
    }
    public void setShowLevels(boolean showLevels) {
        this.showLevels = showLevels;
        repaint();
    }

    // Метод отображения всего компонента, содержащего график
    public void paintComponent(Graphics g) {

        super.paintComponent(g); // Заливка области цветом заднего фона
        if (graphicsData == null || graphicsData.length == 0) // Данных нет - ничего не делать
            return;

        // Определение области пространства, подлежащей отображению
        // Верхний левый угол это (minX, maxY) - правый нижний это (maxX, minY)
        minX = graphicsData[0][0];
        maxX = graphicsData[graphicsData.length - 1][0];
        minY = graphicsData[0][1];
        maxY = minY;

        // Найти минимальное и максимальное значение функции
        for (int i = 1; i < graphicsData.length; i++) {
            if (graphicsData[i][1] < minY) {
                minY = graphicsData[i][1];
            }
            if (graphicsData[i][1] > maxY) {
                maxY = graphicsData[i][1];
            }
        }

        // Определить (исходя из размеров окна) масштабы по осям X и Y
        double scaleX = getSize().getWidth() / (maxX - minX);
        double scaleY = getSize().getHeight() / (maxY - minY);

        // Чтобы изображение было неискаженным - масштаб должен быть одинаков
        // Выбираем за основу минимальный
        scale = Math.min(scaleX, scaleY);

        // Корректировка границ отображаемой области согласно выбранному масштабу
        if (scale == scaleX) {

            // Вычислим, сколько делений влезет по Y при выбранном масштабе и
            // вычтем сколько деление требовалось изначально
            double yIncrement = (getSize().getHeight()/scale - (maxY - minY))/2;
            maxY += yIncrement;
            minY -= yIncrement;
        }
        if (scale == scaleY) {

            // Если за основу был взят масштаб по оси Y, действовать по аналогии
            double xIncrement = (getSize().getWidth()/scale - (maxX - minX))/2;
            maxX += xIncrement;
            minX -= xIncrement;
        }

        // Сохранить текущие настройки холста
        Graphics2D canvas = (Graphics2D) g;
        Stroke oldStroke = canvas.getStroke();
        Color oldColor = canvas.getColor();
        Paint oldPaint = canvas.getPaint();
        Font oldFont = canvas.getFont();

        // Вызов методов отображения элементов графика
        // Порядок вызова методов имеет значение (редыдущий рисунок будет затираться последующим)
        if (showAxis)
            paintAxis(canvas); // Оси координат
        paintGraphics(canvas); // График
        if (showMarkers)
            paintMarkers(canvas); // Маркеры точек

        // Восстановить старые настройки холста
        canvas.setFont(oldFont);
        canvas.setPaint(oldPaint);
        canvas.setColor(oldColor);
        canvas.setStroke(oldStroke);
    }

    // Отрисовка графика по прочитанным координатам
    protected void paintGraphics(Graphics2D canvas) {

        // Линия для рисования графика
        canvas.setStroke(graphicsStroke);
        canvas.setColor(Color.RED);

        GeneralPath graphics = new GeneralPath();
        for (int i = 0; i < graphicsData.length; i++) {

            // Преобразовать значения (x,y) в точку на экране point
            Point2D.Double point = xyToPoint(graphicsData[i][0], graphicsData[i][1]);
            if (i > 0) {

                // Не первая итерация цикла - вести линию в точку point
                graphics.lineTo(point.getX(), point.getY()); // Ввод линии в точку
            }
            else {
                graphics.moveTo(point.getX(), point.getY()); // Начало пути при первой итерации
            }
        }
        canvas.draw(graphics); // Отобразить график
    }

    public static boolean isSequential(double num) {
        String numStr = Double.toString(num);
        for (int i = 0; i < numStr.length() - 1; i++) {
            if (numStr.charAt(i) >= numStr.charAt(i + 1)) {
                return false;
            }
        }
        return true;
    }
    // Отображение маркеров точек, по которым рисовался график
    public boolean isEven (double num){
        if ((int)num %2 ==0){
            return true;
        }
        else return false;
    }

    protected void paintMarkers(Graphics2D canvas) {
        canvas.setStroke(markerStroke);
        for (Double[] point : graphicsData) {
            if (isEven(point[1])) {
                canvas.setColor(Color.RED);
                canvas.setPaint(Color.RED);
            } else {
                canvas.setColor(Color.BLUE);
                canvas.setPaint(Color.BLUE);
            }
            Point2D.Double center = xyToPoint(point[0], point[1]);

            // Draw the quatrefoil-shaped marker with external lines
            double markerSize = 22; // Size of the marker
            double outerRadius = markerSize / 2; // Radius of the outer circle
            double innerRadius = outerRadius * 0.5; // Radius of the inner circle

            Path2D.Double quatrefoil = new Path2D.Double();
            double angle = Math.PI / 4; // Starting angle

            // Create the points of the quatrefoil
            for (int i = 0; i < 4; i++) {
                double outerX = center.getX() - outerRadius * Math.sin(angle+ Math.PI / 4);
                double outerY = center.getY() + outerRadius * Math.cos(angle+ Math.PI / 4);
                double innerX = center.getX() - innerRadius * Math.sin(angle + Math.PI / 2);
                double innerY = center.getY() + innerRadius * Math.cos(angle + Math.PI / 2);

                if (i == 0) {
                    quatrefoil.moveTo(outerX, outerY);
                } else {
                    quatrefoil.lineTo(outerX, outerY);
                }
                quatrefoil.lineTo(innerX, innerY);

                angle += Math.PI / 2;
            }

            quatrefoil.closePath(); // Close the path

            canvas.draw(quatrefoil);
        }

    }



    // Метод, обеспечивающий отображение осей координат
    protected void paintAxis(Graphics2D canvas) {

        canvas.setStroke(axisStroke); // Установить особое начертание для осей
        canvas.setColor(Color.BLACK); // Оси рисуются чѐрным цветом
        canvas.setPaint(Color.BLACK); // Стрелки заливаются чѐрным цветом
        canvas.setFont(axisFont); // Подписи к координатным осям делаются специальным шрифтом

        // Создать объект контекста отображения текста для получения характеристик устройства (экрана)
        FontRenderContext context = canvas.getFontRenderContext();

        // Определить, должна ли быть видна ось Y на графике
        // Она должна быть видна, если левая граница показываемой области (minX) <= 0.0,
        // а правая (maxX) >= 0.0
        if (minX <= 0.0 && maxX >= 0.0) {

            canvas.draw(new Line2D.Double(xyToPoint(0, maxY), xyToPoint(0, minY)));
            GeneralPath arrow = new GeneralPath(); // Стрелка оси Y

            // Установить начальную точку ломаной точно на верхний конец оси Y
            Point2D.Double lineEnd = xyToPoint(0, maxY);
            arrow.moveTo(lineEnd.getX(), lineEnd.getY());

            // Вести левый "скат" стрелки в точку с относительными координатами (5,20)
            arrow.lineTo(arrow.getCurrentPoint().getX() + 5, arrow.getCurrentPoint().getY() + 20);

            // Вести нижнюю часть стрелки в точку с относительными координатами (-10, 0)
            arrow.lineTo(arrow.getCurrentPoint().getX() - 10, arrow.getCurrentPoint().getY());

            arrow.closePath(); // Замкнуть треугольник стрелки
            canvas.draw(arrow); // Нарисовать стрелку
            canvas.fill(arrow); // Закрасить стрелку

            Rectangle2D bounds = axisFont.getStringBounds("y", context); // Подпись к оси Y
            Point2D.Double labelPos = xyToPoint(0, maxY); // Сколько места понадобится для надписи "y"

            // Вывести надпись в точке с вычисленными координатами
            canvas.drawString("y", (float)labelPos.getX() + 10, (float)(labelPos.getY() - bounds.getY()));
            bounds = axisFont.getStringBounds("0", context);
            labelPos = xyToPoint(0, 0);
            // Вывести надпись в точке с вычисленными координатами
            /* canvas.drawString("0", (float) labelPos.getX() + 10,
                    (float) (labelPos.getY() - bounds.getY()));*/
        }

        // Определить, должна ли быть видна ось X на графике
        // Она должна быть видна, если верхняя граница показываемой области (maxX) >= 0.0,
        // а нижняя (minY) <= 0.0
        if (minY <= 0.0 && maxY >= 0.0) {

            canvas.draw(new Line2D.Double(xyToPoint(minX, 0), xyToPoint(maxX, 0)));
            GeneralPath arrow = new GeneralPath(); // Стрелка оси X

            // Установить начальную точку ломаной точно на правый конец оси X
            Point2D.Double lineEnd = xyToPoint(maxX, 0);
            arrow.moveTo(lineEnd.getX(), lineEnd.getY());

            // Вести верхний "скат" стрелки в точку с относительными координатами (-20,-5)
            arrow.lineTo(arrow.getCurrentPoint().getX()-20, arrow.getCurrentPoint().getY()-5);

            // Вести левую часть стрелки в точку с относительными координатами (0, 10)
            arrow.lineTo(arrow.getCurrentPoint().getX(), arrow.getCurrentPoint().getY()+10);

            arrow.closePath(); // Замкнуть треугольник стрелки
            canvas.draw(arrow); // Нарисовать стрелку
            canvas.fill(arrow); // Закрасить стрелку

            Rectangle2D bounds = axisFont.getStringBounds("x", context); // Подпись к оси X
            Point2D.Double labelPos = xyToPoint(maxX, 0); // Cколько места понадобится для надписи "x"

            // Вывести надпись в точке с вычисленными координатами
            canvas.drawString("x", (float)(labelPos.getX() - bounds.getWidth() - 10),
                    (float)(labelPos.getY() + bounds.getY()));
        }
        // OX
        //Double[] specialPoint = {1.0, 0.0};
        // OY
        Double[] specialPoint = {0.0, 1.0};
        Point2D.Double specialCenter = xyToPoint(specialPoint[0], specialPoint[1]);
        Line2D.Double dash = new Line2D.Double();
        // для ОУ
        Point2D.Double dashStart = shiftPoint(specialCenter, -8, -1);
        Point2D.Double dashEnd = shiftPoint(specialCenter, 8, -1);
        // для оси ОХ
        //Point2D.Double dashStart = shiftPoint(specialCenter, 1, 8);
        //Point2D.Double dashEnd = shiftPoint(specialCenter, 1, -8);
        dash.setLine(dashStart, dashEnd);
        canvas.setColor(Color.BLACK);
        canvas.setPaint(Color.BLACK);
        canvas.draw(dash);
        String label = "1";
        Font font = new Font("Arial", Font.ITALIC, 18);
        canvas.setFont(font);
        canvas.setColor(Color.BLACK);
        canvas.drawString(label, (float) dashEnd.getX(), (float) dashEnd.getY());
    }

    /* Метод-помощник, осуществляющий преобразование координат.
    Оно необходимо, т.к. верхнему левому углу холста с координатами
    (0.0, 0.0) соответствует точка графика с координатами (minX, maxY),
    где minX - это самое "левое" значение X, а maxY - самое "верхнее" значение Y.
    */
    protected Point2D.Double xyToPoint(double x, double y) {

        double deltaX = x - minX; // Смещение X от самой левой точки (minX)
        double deltaY = maxY - y; // Смещение Y от точки верхней точки (maxY)
        return new Point2D.Double(deltaX*scale, deltaY*scale);
    }

    /* Метод-помощник, возвращающий экземпляр класса Point2D.Double
    смещённый по отношению к исходному на deltaX, deltaY
    К сожалению, стандартного метода, выполняющего такую задачу, нет.
    */
    protected Point2D.Double shiftPoint(Point2D.Double src, double deltaX, double deltaY) {

        // Инициализировать новый экземпляр точки
        Point2D.Double dest = new Point2D.Double();

        // Задать ее координаты как координаты существующей точки + заданные смещения
        dest.setLocation(src.getX() + deltaX, src.getY() + deltaY);
        return dest;
    }
}