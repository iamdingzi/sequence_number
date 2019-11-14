package com.peng;


import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Peng Xiang
 * @version v1.0
 * @since 2019/11/14
 */
public class SequenceNumber extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        // Get all the required data from data keys
        // Editor and Project were verified in update(), so they are not null.
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        final Document document = editor.getDocument();
        // Work off of the primary caret to get the selection info
        CaretModel caretModel = editor.getCaretModel();
        List<Caret> allCarets = caretModel.getAllCarets();

        // Replace the selection with a fixed string.
        // Must do this document change in a write action context.
        WriteCommandAction.runWriteCommandAction(project, () ->
                {
                    for (int i = 0; i < allCarets.size(); i++) {
                        Caret caret = allCarets.get(i);
                        int start = caret.getSelectionStart();
                        int end = caret.getSelectionEnd();
                        String charSequence = String.valueOf(i);
                        document.replaceString(start, end, charSequence);
                        // De-select the text range that was just replaced
                        caret.removeSelection();
                    }
                }
        );
    }
}
