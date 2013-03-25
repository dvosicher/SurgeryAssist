package com.surgeryassist.util.faces;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.primefaces.context.DefaultRequestContext;
import org.primefaces.util.Constants;

public class Primefaces35PhaseListener implements PhaseListener {

	private static final long serialVersionUID = -3984000725647306385L;

	@Override
	public void afterPhase(PhaseEvent event) {
		// NOP

	}

	@Override
	public void beforePhase(PhaseEvent event) {

		FacesContext context = event.getFacesContext();

        if (context.getAttributes().get(Constants.REQUEST_CONTEXT_ATTR) == null) {
            context.getAttributes().put(Constants.REQUEST_CONTEXT_ATTR, new DefaultRequestContext());
        }
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}

}
