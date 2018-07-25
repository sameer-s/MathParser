package ssuri.cassystem.gui;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ssuri.cassystem.tree.SyntaxTree;
 
public abstract class SyntaxTreeRenderer extends Application 
{    
    private SyntaxTree[] trees;
    public SyntaxTreeRenderer(SyntaxTree... trees)
    {
        this.trees = trees;
    }
    
    @Override
    public void start(Stage primaryStage) 
    {
        primaryStage.setTitle("Syntax Tree Renderer");        
        
        TreeItem<String> largeTreeRoot = new TreeItem<>("Trees");
        TreeView<String> treeView = new TreeView<>(largeTreeRoot);
        largeTreeRoot.setExpanded(true);

        for(SyntaxTree tree : trees)
        {
            TreeItem<String> rootItem = tree == null ? new TreeItem<> ("Tree is null") : tree.getGuiItem();
            largeTreeRoot.getChildren().add(rootItem);
        }
        
        StackPane root = new StackPane();
        root.getChildren().add(treeView);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}

