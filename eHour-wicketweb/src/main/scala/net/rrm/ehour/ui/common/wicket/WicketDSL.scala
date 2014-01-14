package net.rrm.ehour.ui.common.wicket

import org.apache.wicket.ajax.markup.html.form.{AjaxButton => WicketAjaxButton}
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.ajax.{AjaxEventBehavior, AjaxRequestTarget}
import net.rrm.ehour.ui.common.wicket.AjaxButton.Callback
import org.apache.wicket.ajax.markup.html.{AjaxLink => WicketAjaxLink}
import net.rrm.ehour.ui.common.wicket.AjaxLink.LinkCallback
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.{ComponentTag, MarkupStream}
import org.apache.commons.lang.StringUtils
import org.apache.wicket.util.convert.IConverter
import org.apache.wicket.model.{Model => WModel, IModel}
import java.util.Date
import net.rrm.ehour.ui.common.converter.DateConverter
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior
import net.rrm.ehour.ui.common.decorator.LoadingSpinnerDecorator
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes
import net.rrm.ehour.ui.common.session.EhourWebSession
import net.rrm.ehour.ui.common.decorator.{LoadingSpinnerDecorator, DemoDecorator}
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes

case class NonDemoAjaxButton(id: String, form: Form[_], success: AjaxButton.Callback, error: AjaxButton.Callback = (a, f) => {}) extends WicketAjaxButton(id, form) {
  private def inDemoMode = EhourWebSession.getSession.getEhourConfig.isInDemoMode

  override def onSubmit(target: AjaxRequestTarget, form: Form[_]) {
    if (!inDemoMode) {
      success(target, form)
    }
  }

  protected override def updateAjaxAttributes(attributes: AjaxRequestAttributes) {
    super.updateAjaxAttributes(attributes)
    attributes.getAjaxCallListeners.add(if (inDemoMode) new DemoDecorator else new LoadingSpinnerDecorator)
  }

  override def onError(target: AjaxRequestTarget, form: Form[_]) {
    target.add(form)

    error(target, form)
  }
}


case class AjaxButton(id: String, form: Form[_], success: Callback, error: Callback = (a, f) => {}) extends WicketAjaxButton(id, form) {
  override def onSubmit(target: AjaxRequestTarget, form: Form[_]) {
    success(target, form)
  }

  override def onError(target: AjaxRequestTarget, form: Form[_]) {
    error(target, form)
  }
}

object AjaxButton {
  type Callback = (AjaxRequestTarget, Form[_]) => Unit
}


case class NonDemoAjaxLink(id: String, success: LinkCallback) extends WicketAjaxLink(id) {
  private def inDemoMode = EhourWebSession.getSession.getEhourConfig.isInDemoMode


  override def onClick(target: AjaxRequestTarget) {
    if (!inDemoMode) {
      success(target)
    }
  }

  protected override def updateAjaxAttributes(attributes: AjaxRequestAttributes) {
    attributes.getAjaxCallListeners.add(if (inDemoMode) new DemoDecorator else new LoadingSpinnerDecorator)
  }
}


class AjaxLink(id: String, success: LinkCallback) extends WicketAjaxLink(id) {
  override def onClick(target: AjaxRequestTarget) {
    success(target)
  }

  override def updateAjaxAttributes(attributes: AjaxRequestAttributes) {
    super.updateAjaxAttributes(attributes)
    attributes.getAjaxCallListeners.add(new LoadingSpinnerDecorator)
  }

  protected override def updateAjaxAttributes(attributes: AjaxRequestAttributes) {
    super.updateAjaxAttributes(attributes)
    attributes.getAjaxCallListeners.add(new LoadingSpinnerDecorator)
  }

}

object AjaxLink {
  type LinkCallback = AjaxRequestTarget => Unit
}

class Container(id: String) extends WebMarkupContainer(id) {
  setOutputMarkupId(true)
}

class NonEmptyLabel[T <: java.io.Serializable](id: String, label: IModel[T], converter: Option[IConverter[T]] = None) extends Label(id, label) {
  override def getConverter[X](`type`: Class[X]): IConverter[X] = converter match {
    case Some(c) => c.asInstanceOf[IConverter[X]]
    case None => super.getConverter(`type`)
  }

  override def onComponentTagBody(markupStream: MarkupStream, openTag: ComponentTag) = replaceComponentTagBody(markupStream, openTag, if (StringUtils.isBlank(getDefaultModelObjectAsString)) "&nbsp;" else getDefaultModelObjectAsString)
}

class DateLabel(id: String, model: IModel[Date]) extends NonEmptyLabel(id, model, Some(new DateConverter)) {
  override def onComponentTagBody(markupStream: MarkupStream, openTag: ComponentTag): Unit = replaceComponentTagBody(markupStream, openTag, if (StringUtils.isBlank(getDefaultModelObjectAsString)) "&infin;" else getDefaultModelObjectAsString)
}

object WicketDSL {
  def ajaxClick(click: AjaxRequestTarget => Unit): Behavior = new AjaxEventBehavior("onclick") {
    def onEvent(target: AjaxRequestTarget) = click(target)
  }

  def ajaxSubmit(form: Form[Unit], submit: (Form[Unit], AjaxRequestTarget) => Unit): Behavior = new AjaxFormSubmitBehavior(form, "click") {
    override def onSubmit(target: AjaxRequestTarget) = submit(form, target)
  }

  implicit def toModel[T <: java.io.Serializable](m: Model[T]) = new WModel[T](m.model)
}

case class Model[T <: java.io.Serializable](model: T) extends WModel[T](model)

