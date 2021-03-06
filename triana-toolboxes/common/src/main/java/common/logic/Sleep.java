package common.logic;

import org.trianacode.annotation.Process;
import org.trianacode.annotation.SliderParameter;
import org.trianacode.annotation.Tool;

import java.util.List;

/**
 * @author Andrew Harrison
 * @version 1.0.0 Sep 25, 2010
 */
@Tool
public class Sleep {

    @SliderParameter(title = "duration (seconds)")
    private long period = 0;

    @Process(gather = true)
    public List<Object> sleep(List<Object> in) {
        synchronized (this) {
            try {
                if (period > 0) {
                    wait(period * 1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return in;
    }
}
