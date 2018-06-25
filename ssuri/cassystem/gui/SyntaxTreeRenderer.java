package ssuri.cassystem.gui;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ssuri.cassystem.parser.tree.SyntaxTree;
 
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
        
        TreeItem<String> largeTreeRoot = new TreeItem<String>("Trees");
        TreeView<String> treeView = new TreeView<String>(largeTreeRoot);        
        largeTreeRoot.setExpanded(true);

        for(SyntaxTree tree : trees)
        {
            TreeItem<String> rootItem;
            if(tree == null) rootItem = new TreeItem<String> ("Tree is null");
            else if(tree.root == null) rootItem = new TreeItem<String> ("Tree root is null");
            else rootItem = tree.root.getGuiItem();
            largeTreeRoot.getChildren().add(rootItem);
        }
        
        StackPane root = new StackPane();
        root.getChildren().add(treeView);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}

