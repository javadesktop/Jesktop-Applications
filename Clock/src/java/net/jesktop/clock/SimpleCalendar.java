
/*****************************************************************************
 *
 * Unlicenced.work by William Beebe
 *
 *****************************************************************************/
package net.jesktop.clock;



import java.awt.*;

import java.util.*;

import javax.swing.*;


/**
 * Class SimpleCalendar
 *
 *
 * @author William Beebe
 * @version $Revision: 1.1 $
 */
public class SimpleCalendar extends JPanel implements Runnable {

    private Date date = new Date();
    private Calendar calendar = new GregorianCalendar();
    private String[] month_names = {
        "January", "February", "March", "April", "May", "June", "July", "August", "September",
        "October", "November", "December"
    }, column_tops = {
        "s", "m", "t", "w", "t", "f", "s"
    };
    private JLabel month_name = new JLabel(" ", SwingConstants.CENTER);
    private JPanel month_grid = new JPanel(new GridLayout(6, 7, 2, 2));
    private Color background_color, column_text_color, day_text_color;

    /**
     * Constructor SimpleCalendar
     *
     *
     */
    public SimpleCalendar() {

        setPreferredSize(new Dimension(160, 130));
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        setLayout(new BorderLayout());
        add(month_name, BorderLayout.NORTH);
        add(month_grid, BorderLayout.CENTER);
    }

    /* Builds the calendar panel.
     * Updates the Gregorian Calendar with the system time (in milliseconds)
     * via the Date class. Then builds the rows and columns in the month.
     * Today's date is marked with a red border.
     */
    private void build_month_display() {

        int year = calendar.get(Calendar.YEAR), month = calendar.get(Calendar.MONTH);

        setBackground(background_color);
        date.setTime(System.currentTimeMillis());
        calendar.setTime(date);
        month_name.setText(month_names[month] + "  " + Integer.toString(year));
        month_name.setForeground(day_text_color);
        month_grid.setBackground(background_color);

        for (int i = 0; i < 7; i++) {
            JLabel l = new JLabel(column_tops[i], SwingConstants.CENTER);

            l.setForeground(column_text_color);
            month_grid.add(l);
        }

        int today = calendar.get(Calendar.DAY_OF_MONTH),
            weekday_start = calculate_first_of_month(year, month),
            max_days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH), day_count = 1;
        boolean can_start = false;

        for (int wk_cnt = 0; wk_cnt < 5; wk_cnt++) {
            for (int day_cnt = 0; day_cnt < 7; day_cnt++) {
                JLabel day_num;

                if (weekday_start == day_cnt) {
                    can_start = true;
                }

                if (can_start && (day_count <= max_days)) {
                    day_num = new JLabel(Integer.toString(day_count), SwingConstants.CENTER);

                    if (day_count == today) {
                        day_num.setForeground(Color.red);
                        day_num.setBorder(BorderFactory.createLineBorder(day_text_color, 1));
                    } else {
                        day_num.setForeground(day_text_color);
                    }

                    day_count++;
                } else {
                    day_num = new JLabel(" ", SwingConstants.LEFT);
                }

                month_grid.add(day_num);
            }
        }
    }

    // The following portions originally from Michael Bertrand's Java Perpetual Calendar
    // http://www.execpc.com/~mikeber/calendar.html
    int max_days_in_month[] = {
        31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
    };

    /*
     * USE:  Calculates day of the week the first day of the month falls on.
     * IN:   year = given year after 1582 (start of the Gregorian calendar).
     *       month = 0 for January, 1 for February, etc.
     * OUT:  First day of month: 0 = Sunday, 1 = Monday, etc.
     */

    /**
     * Method calculate_first_of_month
     *
     *
     * @param year
     * @param month
     *
     * @return
     *
     */
    int calculate_first_of_month(int year, int month) {

        int first_day,    // day of week for Jan 1, then first day of month
            i;            // to traverse months before given month

        if ((year < 1582) || (month < 0) || (month > 11)) {
            return -1;
        }

        /* Get day of week for Jan 1 of given year. */
        first_day = calculate_january_first(year);

        /* Increase first_day by days in year before given month to get first day
         * of month.
         */
        for (i = 0; i < month; i++) {
            first_day += max_days_in_month[i];
        }

        /* Increase by one if month after February and leap year. */
        if ((month > 1) && is_leap_year(year)) {
            first_day++;
        }

        /* Convert to day of the week and return. */
        return (first_day % 7);
    }

    /*
     * USE:  Determines if given year is a leap year.
     * IN:   year = given year after 1582 (start of the Gregorian calendar).
     * OUT:  TRUE if given year is leap year, FALSE if not.
     * NOTE: Formulas capture definition of leap years; cf calculate_leap_years().
     */

    /**
     * Method is_leap_year
     *
     *
     * @param year
     *
     * @return
     *
     */
    boolean is_leap_year(int year) {

        /* If multiple of 100, leap year iff multiple of 400. */
        if ((year % 100) == 0) {
            return ((year % 400) == 0);
        }

        /* Otherwise leap year iff multiple of 4. */
        return ((year % 4) == 0);
    }

    /*
     * USE:  Calculate day of the week on which January 1 falls for given year.
     * IN:   year = given year after 1582 (start of the Gregorian calendar).
     * OUT:  Day of week for January 1: 0 = Sunday, 1 = Monday, etc.
     * NOTE: Formula starts with a 5, since January 1, 1582 was a Friday; then
     *       advances the day of the week by one for every year, adding the
     *       number of leap years intervening, because those years Jan 1
     *       advanced by two days. Calculate mod 7 to get the day of the week.
     */

    /**
     * Method calculate_january_first
     *
     *
     * @param year
     *
     * @return
     *
     */
    int calculate_january_first(int year) {

        if (year < 1582) {
            return -1;    // Start at 1582, when modern calendar starts.
        }

        /* Start Fri 01-01-1582; advance a day for each year, 2 for leap yrs. */
        return ((5 + (year - 1582) + calculate_leap_years(year)) % 7);
    }

    /*
     * USE:  Calculate number of leap years since 1582.
     * IN:   year = given year after 1582 (start of the Gregorian calendar).
     * OUT:  number of leap years since the given year, -1 if year < 1582
     * NOTE: Count doesn't include the given year if it is a leap year.
     *       In the Gregorian calendar, used since 1582, every fourth year
     *       is a leap year, except for years that are a multiple of a
     *       hundred, but not a multiple of 400, which are no longer leap
     *       years. Years that are a multiple of 400 are still leap years:
     *       1700, 1800, 1990 were not leap years, but 2000 will be.
     */

    /**
     * Method calculate_leap_years
     *
     *
     * @param year
     *
     * @return
     *
     */
    int calculate_leap_years(int year) {

        int leap_years,       // number of leap years to return
            hundreds,         // number of years multiple of a hundred
            four_hundreds;    // number of years multiple of four hundred

        if (year < 1582) {
            return -1;
        }

        /* Calculate number of years in interval that are a multiple of 4. */
        leap_years = (year - 1581) / 4;

        /* Calculate number of years in interval that are a multiple of 100;
         * subtract, since they are not leap years.
         */
        hundreds = (year - 1501) / 100;
        leap_years -= hundreds;

        /* Calculate number of years in interval that are a multiple of 400;
         * add back in, since they are still leap years.
         */
        four_hundreds = (year - 1201) / 400;
        leap_years += four_hundreds;

        return leap_years;
    }

    /**
     * Method init
     *
     *
     */
    public void init() {

        background_color = Color.decode("0x66000080");
        column_text_color = Color.decode("0xcceeff");
        day_text_color = Color.decode("0xcceeff");

        build_month_display();
    }

    /**
     * Method run
     *
     *
     */
    public void run() {}

    /**
     * Method start
     *
     *
     */
    public void start() {}

    /**
     * Method stop
     *
     *
     */
    public void stop() {}

    /**
     * Method update
     *
     *
     * @param g
     *
     */
    public void update(Graphics g) {
        paint(g);
    }
}
