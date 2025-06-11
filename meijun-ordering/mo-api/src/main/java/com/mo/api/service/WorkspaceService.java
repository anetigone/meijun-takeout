package com.mo.api.service;

import com.mo.api.vo.OrderOverviewVO;
import com.mo.api.vo.WorkspaceDataVO;

import java.time.LocalDateTime;

public interface WorkspaceService {

    WorkspaceDataVO getWorkspaceData(LocalDateTime begin, LocalDateTime end);

    OrderOverviewVO getOrderOverview();
}
