package io.github.design.mybatis.associations;

import io.github.design.CheckTask;
import io.github.design.CheckTaskDetail;
import io.github.design.ContainerNo;
import io.github.design.mybatis.ModelMapper;

import javax.inject.Inject;
import java.util.List;

public class TaskDetails implements CheckTask.Details {
    @Inject
    private ModelMapper modelMapper;

    private final CheckTask task;

    public TaskDetails(CheckTask checkTask) {
        this.task = checkTask;
    }

    @Override
    public List<CheckTaskDetail> listBy(ContainerNo containerNo) {
        return modelMapper.findCheckTaskDetailsByContainer(containerNo.value());
    }
}
