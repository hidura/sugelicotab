package sugelico.postabsugelico;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.HashMap;

import sugelico.postabsugelico.General.Definitions.CartProds;
import sugelico.postabsugelico.General.Definitions.CatalogCat;
import sugelico.postabsugelico.General.Definitions.Products;
import sugelico.postabsugelico.General.Definitions.UserProfile;
import sugelico.postabsugelico.General.Definitions.cmpDetails;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link products.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link products#newInstance} factory method to
 * create an instance of this fragment.
 */
public class products extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public products() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment products.
     */
    // TODO: Rename and change types and number of parameters
    public static products newInstance(String param1, String param2) {
        products fragment = new products();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_products, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void onTrackSelected(UserProfile profile,
                                HashMap<Integer, CatalogCat> category,
                                HashMap<Long, CartProds> shoppingCart,
                                HashMap<Integer, Products> products,
                                cmpDetails companyProfile) {
        RecyclerView recycler=null;
        if (category!=null) {
            ((RecyclerView) getActivity().findViewById(R.id.products_list)).setVisibility(View.GONE);
            recycler = ((RecyclerView) getActivity().findViewById(R.id.subcategories_lst));
            recycler.setAdapter(new subcategory_adapter(getActivity(), profile, category, shoppingCart,companyProfile));
        }else{
            ((RecyclerView) getActivity().findViewById(R.id.subcategories_lst)).setVisibility(View.GONE);
            recycler = ((RecyclerView) getActivity().findViewById(R.id.products_list));
            recycler.setAdapter(new product_adapter(getActivity(), profile, products, shoppingCart, companyProfile));
        }
        GridLayoutManager manager = new GridLayoutManager(getContext(),3); // MAX NUMBER OF SPACES
        recycler.setLayoutManager(manager);
        recycler.setVisibility(View.VISIBLE);
        ((LinearLayout)getActivity().findViewById(R.id.progress_div3)).setVisibility(View.GONE);

    }
}
