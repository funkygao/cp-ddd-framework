package io.github.design;

import io.github.dddplus.dsl.KeyFlow;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CheckTaskFinishedListener {

    @EventListener(classes = CheckTaskFinished.class)
    @KeyFlow(async = true)
    public void onTaskFinished(CheckTaskFinished event) {

    }
}
